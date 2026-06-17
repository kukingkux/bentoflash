package com.tup.bentoflash.core.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.bentoflash.core.dto.MarketplaceDTOs.OrderResponse;
import com.tup.bentoflash.core.dto.MarketplaceDTOs.ReservationRequest;


@RestController
@RequestMapping("/api/bento-market")
@CrossOrigin(origins = "http://localhost:3000")
public class BentoMarketController {
    
    @GetMapping("/catalog")
    public ResponseEntity<List<Object>> getCatalog() {
        // Mimics Polymorphic structural returns matching database profiles
        List<Object> structuralCatalog = new ArrayList<>();
        
        // Mock LocalCulturalBento
        structuralCatalog.add(java.util.Map.of(
            "skuCode", "TT-BNTO-RENDANG-01",
            "name", "Nasi Padang Rendang",
            "basePrice", 20000.0,
            "currentPrice", 20000.0, // Drop ke 16000 via Elfan's task downstream
            "calorieCount", 545,
            "packagingType", "Eco-friendly Cardboard Box"
        ));

        // Mock GrabAndGoBeverage
        structuralCatalog.add(java.util.Map.of(
            "skuCode", "TT-BVG-ESTEH-02",
            "name", "Es Teh Manis",
            "basePrice", 5000.0,
            "currentPrice", 5000.0,
            "isRefrigerated", true,
            "packagingType", "Recyclable PET Bottle"
        ));

        return ResponseEntity.ok(structuralCatalog);
    }

    @PostMapping("/reserve")
    public ResponseEntity<OrderResponse> reserveBento(@RequestBody ReservationRequest request) {
        // Simulates Receipt. Phase 3 hooks: queueManager.addToQueue(order)
        OrderResponse mockOrder = new OrderResponse(104, request.getSkuCode(), "PENDING", false, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(mockOrder);
    }
}
