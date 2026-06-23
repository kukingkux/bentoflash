package com.tup.bentoflash.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.tup.bentoflash.core.model.Order;

@Service
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
                order.setStatus("READY");
                System.out.println("[SISTEM] Pesanan dengan kode " + pickupCode + " sudah SIAP diambil!");
                isFound = true;
                break;
            }
        }
        
        if (!isFound) {
            System.out.println("[ERROR] Pesanan dengan kode " + pickupCode + " tidak ditemukan di antrean aktif.");
        }
    }

    //Menandai pesanan di antrean bahwa sudah diambil (tanpa dihapus).
    public void markDone(String pickupCode) {
        boolean isFound = false;
        
        for (Order order : activeOrders) {
            if (order.getPickupCode().equals(pickupCode)) {
                order.setPickedUp(true);
                order.setStatus("CLAIMED");
                System.out.println("[SISTEM] Pesanan dengan kode " + pickupCode + " telah DIAMBIL.");
                isFound = true;
                break;
            }
        }
        
        if (!isFound) {
            System.out.println("[ERROR] Pesanan dengan kode " + pickupCode + " tidak ditemukan di antrean aktif saat claim.");
        }
    }

    // getter activeOrders
    public java.util.Queue<Order> getActiveOrders() {
        return this.activeOrders;
    }
    
    // (Opsional) Method untuk melihat isi antrean saat ini.
    public void displayQueue() {
        System.out.println("=== Antrean Aktif Dapur ===");
        if (activeOrders.isEmpty()) {
            System.out.println("Antrean kosong.");
        } else {
            for (Order order : activeOrders) {
                System.out.println("- " + order.getPickupCode() + " | Status: " + order.getStatus());
            }
        }
    }
}