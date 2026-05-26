package com.tup.bentoflash.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tup.bentoflash.core.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Rafael bisa fetch pending orders untuk FIFO
    List<Order> findByStatusOrderByOrderTimeAsc(String status);
}
