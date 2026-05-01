package com.winter.inventoryservice.repository;

import java.util.Optional;

import com.winter.inventoryservice.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByProductId(Long productId);
}
