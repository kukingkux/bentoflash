package com.tup.bentoflash.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(name = "name")
    private String username;

    private String email;
    private int karmaScore = 100;

    public User() {}

    public boolean login(String token) {
        // Mock auth for frontend (nextjs)
        return token != null && !token.isEmpty();
    }

    // Getters and Setters
    public Long getId() { return userId; }
    public void setId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getKarmaScore() { return karmaScore; }
    public void setKarmaScore(int karmaScore) { this.karmaScore = karmaScore; }
}
