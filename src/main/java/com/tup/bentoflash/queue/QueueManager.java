package com.tup.bentoflash.queue;

public class QueueManager {
    private Queue<Order> activeOrders;

    // Constructor untuk menginisialisasi queue
    public QueueManager() {
        this.activeOrders = new LinkedList<>();
    }

    //Generate kode pengambilan acak.
    public String generatePickupCode() {
        Random random = new Random();
        int number = random.nextInt(10000); // Menghasilkan angka 0 - 9999
        return String.format("BENTO-%04d", number);
    }

    //Tambah pesanan baru ke dalam antrean.
    public void addToQueue(Order order) {
        if (order != null) {
            activeOrders.add(order);
            System.out.println("[SISTEM] Pesanan ditambahkan ke antrean. Kode Pickup: " + order.getPickupCode());
        }
    }

    //Mencari pesanan berdasarkan pickupCode dan mengubah statusnya menjadi siap.
    public void markReady(String pickupCode) {
        boolean isFound = false;
        
        for (Order order : activeOrders) {
            if (order.getPickupCode().equals(pickupCode)) {
                order.setReady(true);
                System.out.println("[SISTEM] Pesanan dengan kode " + pickupCode + " sudah SIAP diambil!");
                isFound = true;
                break;
            }
        }
        
        if (!isFound) {
            System.out.println("[ERROR] Pesanan dengan kode " + pickupCode + " tidak ditemukan di antrean aktif.");
        }
    }
    
    // (Opsional) Method untuk melihat isi antrean saat ini.
    public void displayQueue() {
        System.out.println("=== Antrean Aktif Dapur ===");
        if (activeOrders.isEmpty()) {
            System.out.println("Antrean kosong.");
        } else {
            for (Order order : activeOrders) {
                System.out.println("- " + order.getPickupCode() + " | Status Siap: " + order.isReady());
            }
        }
    }
}