package com.tup.bentoflash.core.dto;

public class MarketplaceDTOs {
    public static class ReservationRequest {
        private int userId;
        private String skuCode;
        private int quantity;

        public int getUserId() {
            return userId;
        }

        public String getSkuCode() {
            return skuCode;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public static class OrderResponse {
        private int orderId;
        private String skuCode;
        private String status;
        private boolean isPickedUp;
        private String pickupCode;

        public OrderResponse(int orderId, String skuCode, String status, boolean isPickedUp, String pickupCode) {
            this.orderId = orderId;
            this.skuCode = skuCode;
            this.status = status;
            this.isPickedUp = isPickedUp;
            this.pickupCode = pickupCode;
        }

        public int getOrderId() {
            return orderId;
        }

        public String getSkuCode() {
            return skuCode;
        }

        public String getStatus() {
            return status;
        }

        public boolean isPickedUp() {
            return isPickedUp;
        }

        public String getPickupCode() {
            return pickupCode;
        }
    }
}
