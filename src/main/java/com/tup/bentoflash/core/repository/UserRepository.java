package com.tup.bentoflash.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tup.bentoflash.core.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
