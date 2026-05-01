package com.winter.inventoryservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "inventory_items")

public class InventoryItem {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false, unique = true)

    private Long productId;

    @Column(nullable = false)

    private Integer quantity;

    public InventoryItem() {}

    public InventoryItem(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }


    public void decreaseQuantity(int amount) {
        if (this.quantity < amount) {
            throw new IllegalStateException("Not enough stock");
        }

        this.quantity -= amount;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public int getAvailableQuantity() {
        return this.quantity;
    }

}
