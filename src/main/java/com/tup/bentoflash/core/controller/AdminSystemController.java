package com.tup.bentoflash.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.bentoflash.core.model.CatalogItem;
import com.tup.bentoflash.core.repository.CatalogItemRepository;
import com.tup.bentoflash.core.repository.UserRepository;
import com.tup.bentoflash.karma.KarmaEngine;
import com.tup.bentoflash.pricing.FlashPricingEngine;

@RestController
@RequestMapping("/api/admin/system")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminSystemController {

    @Autowired
    private FlashPricingEngine flashPricingEngine;

    @Autowired
    private KarmaEngine karmaEngine;

    @Autowired
    private CatalogItemRepository catalogItemRepository;

    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/cron/trigger-discount")
    public ResponseEntity<Map<String, Object>> forceTimeLeapToFourteen() {
        // set discount hour to 14:00
        flashPricingEngine.setDiscountStartHour(14);
        
        // get all items
        List<CatalogItem> allItems = catalogItemRepository.findAll();
        List<CatalogItem> discountedItems = flashPricingEngine.applyFlashDiscounts(allItems);

        // save discounted items
        catalogItemRepository.saveAll(discountedItems);

        return ResponseEntity.ok(Map.of(
            "status", "SUCCESS",
            "simulatedTime", "14:00 PM",
            "itemsAffectedCount", discountedItems.size(),
            "message", "FlashPricingEngine executed via instanceof IPerishable. 40% discount applied."
        ));
    }

    @GetMapping("/karma-logs")
    public ResponseEntity<List<Map<String, Object>>> getKarmaLogs() {
        return ResponseEntity.ok(List.of(
            Map.of("userId", 3, "username", "Ahmed Guntir", "karmaScore", 120, "status", "GOOD"),
            Map.of("userId", 4, "username", "Dodit Ready", "karmaScore", 70, "status", "BAD")
        ));
    }
}
