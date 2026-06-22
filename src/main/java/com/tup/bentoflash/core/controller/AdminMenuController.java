package com.tup.bentoflash.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.bentoflash.core.model.CatalogItem;
import com.tup.bentoflash.core.model.GrabAndGoBeverage;
import com.tup.bentoflash.core.model.LocalCultureBento;
import com.tup.bentoflash.core.repository.CatalogItemRepository;

@RestController
@RequestMapping("/api/admin/menu")
@CrossOrigin(origins = "http://localhost:3000/")
public class AdminMenuController {

    @Autowired
    private CatalogItemRepository catalogItemRepository;

    @PostMapping
    public ResponseEntity<?> createMenu(@RequestBody Map<String, Object> payload) {
        try {
            String type = payload.getOrDefault("itemType", "BENTO").toString();
            CatalogItem newItem;

            if ("BENTO".equalsIgnoreCase(type)) {
                LocalCultureBento bento = new LocalCultureBento();
                if (payload.containsKey("calorieCount")) {
                    bento.setCalorieCount(Integer.parseInt(payload.get("calorieCount").toString()));
                }
                newItem = bento;
            } else {
                GrabAndGoBeverage beverage = new GrabAndGoBeverage();
                if (payload.containsKey("isRefrigerated")) {
                    beverage.setIsRefrigerated(Boolean.parseBoolean(payload.get("isRefrigerated").toString()));
                }
                newItem = beverage;
            }

            newItem.setName((String) payload.get("name"));
            newItem.setBasePrice(Double.parseDouble(payload.get("basePrice").toString()));
            newItem.setCurrentPrice(newItem.getBasePrice());
            newItem.setSkuCode((String) payload.get("skuCode"));

            CatalogItem savedItem = catalogItemRepository.save(newItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<CatalogItem>> getAllMenu() {
        return ResponseEntity.ok(catalogItemRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMenu(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        CatalogItem existing = catalogItemRepository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Menu tidak ditemukan"));
        }

        try {
            if (payload.containsKey("name")) existing.setName((String) payload.get("name"));
            if (payload.containsKey("skuCode")) existing.setSkuCode((String) payload.get("skuCode"));
            if (payload.containsKey("basePrice")) {
                double newPrice = Double.parseDouble(payload.get("basePrice").toString());
                existing.setBasePrice(newPrice);
                existing.setCurrentPrice(newPrice);
            }

            if (existing instanceof LocalCultureBento bento && payload.containsKey("calorieCount")) {
                bento.setCalorieCount(Integer.parseInt(payload.get("calorieCount").toString()));
            } else if (existing instanceof GrabAndGoBeverage beverage && payload.containsKey("isRefrigerated")) {
                beverage.setIsRefrigerated(Boolean.parseBoolean(payload.get("isRefrigerated").toString()));
            }

            return ResponseEntity.ok(catalogItemRepository.save(existing));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
        if (!catalogItemRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Menu tidak ditemukan"));
        }
        catalogItemRepository.deleteById(id);
        
        return ResponseEntity.ok(Map.of(
            "status", "SUCCESS",
            "message", "Menu dengan ID " + id + " berhasil dihapus dari sistem."
        ));
    }
}