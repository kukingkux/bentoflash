package com.tup.bentoflash.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tup.bentoflash.core.model.CatalogItem;

public interface CatalogItemRepository extends JpaRepository<CatalogItem, Long>{
    // Elfan bisa fetch item yang implement IPerishable
    @Query("SELECT c FROM CatalogItem c")
    List<CatalogItem> findAllItems();
}
