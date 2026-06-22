package com.tup.bentoflash.core.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.bentoflash.calculator.NutritionalCalculator;
import com.tup.bentoflash.core.dto.MarketplaceDTOs.OrderResponse;
import com.tup.bentoflash.core.dto.MarketplaceDTOs.ReservationRequest;
import com.tup.bentoflash.core.model.CatalogItem;
import com.tup.bentoflash.core.model.LocalCultureBento;
import com.tup.bentoflash.core.repository.CatalogItemRepository;
import com.tup.bentoflash.core.repository.OrderRepository;
import com.tup.bentoflash.core.repository.UserRepository;
import com.tup.bentoflash.queue.QueueManager;


@RestController
@RequestMapping("/api/bento-market")
@CrossOrigin(origins = "http://localhost:3000")
public class BentoMarketController {

    @Autowired
    private CatalogItemRepository catalogItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private QueueManager queueManager;

    @Autowired
    private NutritionalCalculator nutritionalCalculator;
    
    @GetMapping("/catalog")
    public ResponseEntity<List<Map<String, Object>>> getCatalog() {
        List<CatalogItem> items = catalogItemRepository.findAll();
        List<Map<String, Object>> responseCatalog = new ArrayList<>();

        for (CatalogItem item : items) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("skuCode", item.getSkuCode());
            map.put("name", item.getName());
            map.put("basePrice", item.getBasePrice());
            map.put("currentPrice", item.getCurrentPrice());
            map.put("packagingType", item.getPackagingType());

            // Pembuktian Polimorfisme & Agregasi Modul Hafidh
            if (item instanceof LocalCultureBento bento) {
                // Hitung total kalori secara dinamis dari tumpukan Ingredient bento tersebut
                double totalCal = nutritionalCalculator.calculateTotalCalories(bento);
                String macros = nutritionalCalculator.calculateMacros(bento);
                
                map.put("calorieCount", totalCal);
                map.put("macros", macros);
                map.put("isPerishable", true);
                map.put("discountApplied", bento.isDiscountApplied());
            } else {
                map.put("isPerishable", false);
            }
            responseCatalog.add(map);
        }
        return ResponseEntity.ok(responseCatalog);
    }


    @PostMapping("/reserve")
    public ResponseEntity<OrderResponse> reserveBento(@RequestBody ReservationRequest request) {
        // Simulates Receipt. Phase 3 hooks: queueManager.addToQueue(order)
        OrderResponse mockOrder = new OrderResponse(104, request.getSkuCode(), "PENDING", false, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(mockOrder);
    }
}
