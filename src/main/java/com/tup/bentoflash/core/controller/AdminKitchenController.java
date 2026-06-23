package com.tup.bentoflash.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.bentoflash.core.dto.AdminDTOs.StockUpdateRequest;
import com.tup.bentoflash.core.dto.MarketplaceDTOs.OrderResponse;
import com.tup.bentoflash.core.model.Order;
import com.tup.bentoflash.core.repository.OrderRepository;
import com.tup.bentoflash.queue.QueueManager;

@RestController
@RequestMapping("/api/admin/kitchen")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdminKitchenController {

    @Autowired
    private QueueManager queueManager;

    @Autowired
    private OrderRepository orderRepository;
    
    @GetMapping("/queue/active")
    public ResponseEntity<List<OrderResponse>> getActiveKitchenQueue() {
        List<OrderResponse> activeQueueResponses = new ArrayList<>();
        
        for (Order order : queueManager.getActiveOrders()) {
            activeQueueResponses.add(new OrderResponse(
                order.getId().intValue(),
                order.getItem().getSkuCode(),
                order.getItem().getName(),
                order.getUser().getId().intValue(),
                order.getOrderTime(),
                order.getStatus(),
                order.isPickedUp(),
                order.getPickupCode()
            ));
        }
        return ResponseEntity.ok(activeQueueResponses);
    }

    @PutMapping("/queue/{orderId}/ready")
    public ResponseEntity<?> markOrderReady(@PathVariable int orderId) {
        Order order = orderRepository.findById((long) orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Order ID tidak valid"));
        }

        // Panggil mutator dari modul milik Rafael untuk merubah status antrean internal
        queueManager.markReady(order.getPickupCode());

        // Sinkronisasi status objek ke database
        order.setStatus("READY");
        Order updatedOrder = orderRepository.save(order);

        OrderResponse response = new OrderResponse(
            updatedOrder.getId().intValue(),
            updatedOrder.getItem().getSkuCode(),
            updatedOrder.getStatus(),
            updatedOrder.isPickedUp(),
            updatedOrder.getPickupCode()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/queue/{orderId}/pickup")
    public ResponseEntity<?> markOrderPickedUp(@PathVariable int orderId) {
        Order order = orderRepository.findById((long) orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Order ID tidak valid"));
        }

        order.setPickedUp(true);
        order.setStatus("CLAIMED");
        Order updatedOrder = orderRepository.save(order);

        // Sync Rafael's queue module!
        queueManager.markDone(updatedOrder.getPickupCode());

        OrderResponse response = new OrderResponse(
            updatedOrder.getId().intValue(),
            updatedOrder.getItem().getSkuCode(),
            updatedOrder.getStatus(),
            updatedOrder.isPickedUp(),
            updatedOrder.getPickupCode()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/items/{skuCode}/stock")
    public ResponseEntity<Void> updateStockLevel(@PathVariable String skuCode, @RequestBody StockUpdateRequest request) {
        return ResponseEntity.noContent().build();
    }
}
