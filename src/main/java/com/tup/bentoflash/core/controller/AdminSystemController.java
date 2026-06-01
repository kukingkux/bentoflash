package com.tup.bentoflash.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/system")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminSystemController {
    
    @PostMapping("/cron/trigger-discount")
    public ResponseEntity<Map<String, Object>> forceTimeLeapToFourteen() {
        return ResponseEntity.ok(Map.of(
            "status", "SUCCESS",
            "simulatedTime", "14:00 PM",
            "message", "FlashPricingEngine executed across all active IPerishable items. 40% Discounts Up"
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
