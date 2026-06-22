package com.tup.bentoflash.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.bentoflash.core.model.CatalogItem;
import com.tup.bentoflash.core.model.User;
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

    // ghosting user simulation
    @PutMapping("/users/{userId}/ghost")
    public ResponseEntity<?> simulateGhostUser(@PathVariable int userId) {
        User user = userRepository.findById((long) userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User tidak ada"));
        }

        int currentScore = user.getKarmaScore();
        int updatedScore = karmaEngine.calculateNewScore(currentScore, false);
        
        user.setKarmaScore(updatedScore);
        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(Map.of(
            "userId", savedUser.getId(),
            "username", savedUser.getUsername(),
            "previousKarmaScore", currentScore,
            "newKarmaScore", savedUser.getKarmaScore(),
            "isCritical", karmaEngine.isScoreCritical(savedUser.getKarmaScore())
        ));
    }

    @GetMapping("/karma-logs")
    public ResponseEntity<List<Map<String, Object>>> getKarmaLogs() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> logs = new ArrayList<>();

        for (User u : users) {
            logs.add(Map.of(
                "userId", u.getId(),
                "username", u.getUsername(),
                "karmaScore", u.getKarmaScore(),
                "status", u.getKarmaScore() > 70 ? "GOOD" : "BAD"
            ));
        }
        return ResponseEntity.ok(logs);
    }
}
