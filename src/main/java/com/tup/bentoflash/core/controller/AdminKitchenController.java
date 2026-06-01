package com.tup.bentoflash.core.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tup.bentoflash.core.dto.AdminDTOs.StockUpdateRequest;
import com.tup.bentoflash.core.dto.MarketplaceDTOs.OrderResponse;

public class AdminKitchenController {
    
    @GetMapping("/queue/active")
    public ResponseEntity<List<OrderResponse>> getActiveKitchenQueue() {
        return ResponseEntity.ok(List.of(
            new OrderResponse(101, "TT-BNTO-RENDANG-01", "PENDING", false, null),
            new OrderResponse(102, "TT-BVG-ESTEH-02", "PENDING", false, null)
        ));
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
