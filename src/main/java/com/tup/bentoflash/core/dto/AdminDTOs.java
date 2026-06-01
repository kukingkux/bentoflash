package com.tup.bentoflash.core.dto;

public class AdminDTOs {
    public static class StockUpdateRequest {
        private int availableStock;

        public int getAvailableStock() {
            return availableStock;
        }

        public void setAvailableStock(int availableStock) {
            this.availableStock = availableStock;
        }
    }
}
