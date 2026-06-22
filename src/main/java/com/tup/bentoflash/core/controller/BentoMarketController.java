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
import com.tup.bentoflash.core.model.Order;
import com.tup.bentoflash.core.model.User;
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

            // Count calorie and macros jika item LocalCultureBento
            if (item instanceof LocalCultureBento bento) {
                // Hitung total kalori
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
    public ResponseEntity<?> reserveBento(@RequestBody ReservationRequest request) {
        // 1. Validasi User
        User user = userRepository.findById((long) request.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User tidak ditemukan"));
        }

        // 2. Validasi Item via SKU
        CatalogItem item = catalogItemRepository.findAll().stream()
                .filter(i -> i.getSkuCode().equals(request.getSkuCode()))
                .findFirst()
                .orElse(null);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Bento dengan SKU tersebut tidak ditemukan"));
        }

        // 3. Buat Object Order Baru & Sambungkan ke Antrean FIFO Rafael
        Order order = new Order();
        order.setUser(user);
        order.setItem(item);
        order.setStatus("PENDING");
        
        // Get unique code
        String uniqueCode = queueManager.generatePickupCode();
        order.setPickupCode(uniqueCode);

        // 4. Persist ke Database Antrean Dapur
        Order savedOrder = orderRepository.save(order);
        queueManager.addToQueue(savedOrder);

        // 5. Convert Long to int for frontend
        OrderResponse response = new OrderResponse(
                savedOrder.getId().intValue(),
                savedOrder.getItem().getSkuCode(),
                savedOrder.getStatus(),
                savedOrder.isPickedUp(),
                savedOrder.getPickupCode()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
