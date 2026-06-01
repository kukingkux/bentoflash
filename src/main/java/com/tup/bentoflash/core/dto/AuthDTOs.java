package com.tup.bentoflash.core.dto;

public class AuthDTOs {
    public static class LoginRequest {
        private String token;

        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class AuthResponse {
        private int userId;
        private String username;
        private String role;
        private int karmaScore;

        public AuthResponse(int userId, String username, String role, int karmaScore) {
            this.userId = userId;
            this.username = username;
            this.role = role;
            this.karmaScore = karmaScore;
        }
        
        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }

        public int getKarmaScore() {
            return karmaScore;
        }
    }
}
