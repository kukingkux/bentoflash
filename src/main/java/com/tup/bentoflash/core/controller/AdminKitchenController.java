package com.tup.bentoflash.core.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin(origins = "*")
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
                order.getStatus(),
                order.isPickedUp(),
                order.getPickupCode()
            ));
        }
        return ResponseEntity.ok(activeQueueResponses);
    }

    @PutMapping("/queue/{orderId}/ready")
    public ResponseEntity<OrderResponse> markOrderReady(@PathVariable int orderId) {
        OrderResponse readyOrder = new OrderResponse(orderId, "TT-BNTO-RENDANG-01", "READY", false, "BNTO-XYZ27");
        return ResponseEntity.ok(readyOrder);
    }

    @PatchMapping("/items/{skuCode}/stock")
    public ResponseEntity<Void> updateStockLevel(@PathVariable String skuCode, @RequestBody StockUpdateRequest request) {
        return ResponseEntity.noContent().build();
    }
}
