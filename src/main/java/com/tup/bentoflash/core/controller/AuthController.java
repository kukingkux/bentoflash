package com.tup.bentoflash.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.bentoflash.core.dto.AuthDTOs.AuthResponse;
import com.tup.bentoflash.core.dto.AuthDTOs.LoginRequest;
import com.tup.bentoflash.core.model.User;
import com.tup.bentoflash.core.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> simulateLogin(@RequestBody LoginRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }

        // Swap roles based on token strings
        if ("kitchen-token".equals(request.getToken())) {
            return ResponseEntity.ok(new AuthResponse(1, "Chef Garcia", "KITCHEN_STAFF", 100));
        } else if ("admin-token".equals(request.getToken())) {
            return ResponseEntity.ok(new AuthResponse(2, "Atmin", "SYSTEM_ADMIN", 200));
        }

        // Defaults standard customer
        User user = userRepository.findById(2L).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(new AuthResponse(user.getId().intValue(), user.getUsername(), "CUSTOMER", user.getKarmaScore()));
        }

        return ResponseEntity.ok(new AuthResponse(3, "Ahmed Guntir", "CUSTOMER", 120));
    }
}
