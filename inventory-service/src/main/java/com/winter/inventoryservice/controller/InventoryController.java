package com.winter.inventoryservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.winter.inventoryservice.dto.ReserveStockRequest;
import com.winter.inventoryservice.model.InventoryItem;
import com.winter.inventoryservice.model.StockReservation;
import com.winter.inventoryservice.service.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
 private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/{productId}")
    public InventoryItem getStock(@PathVariable Long productId) {
        return service.getByProductId(productId);
    }

    @PostMapping
    public InventoryItem createOrUpdate(
            @RequestParam Long productId,
            @RequestParam int quantity

    ) {
        return service.createOrUpdate(productId, quantity);
    }

    @PostMapping("/{productId}/decrease")
    public InventoryItem decrease(
            @PathVariable Long productId,
            @RequestParam int amount
    ) {
        return service.decreaseStock(productId, amount);
    }

    @PostMapping("/{productId}/increase")
    public InventoryItem increase(
            @PathVariable Long productId,
            @RequestParam int amount

    ) {
        return service.increaseStock(productId, amount);
    }

    @GetMapping("/{productId}/available")
    public boolean isAvailable(
            @PathVariable Long productId,
            @RequestParam int quantity
    ) {
        return service.isAvailable(productId, quantity);
    }


    @PostMapping("/reserve")
    public StockReservation reserve(@RequestBody ReserveStockRequest request) {
        return service.reserve(
                request.getProductId(),
                request.getOrderId(),
                request.getQuantity()
        );
    }

    @PostMapping("/release/{orderId}")
    public void release(@PathVariable Long orderId) {
        service.releaseByOrder(orderId);
    }

    @PostMapping("/confirm/{orderId}")
    public void confirm(@PathVariable Long orderId) {
        service.confirmByOrder(orderId);
    }
}
