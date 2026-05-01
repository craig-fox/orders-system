package com.winter.inventoryservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.winter.inventoryservice.model.InventoryItem;
import com.winter.inventoryservice.model.ReservationStatus;
import com.winter.inventoryservice.model.StockReservation;
import com.winter.inventoryservice.repository.InventoryRepository;
import com.winter.inventoryservice.repository.StockReservationRepository;

@Service
public class InventoryService {


    private final InventoryRepository inventoryRepository;
    private final StockReservationRepository reservationRepository;

    public InventoryService(InventoryRepository inventoryRepository, StockReservationRepository reservationRepository) {
        this.inventoryRepository = inventoryRepository;
        this.reservationRepository = reservationRepository;
    }

    public InventoryItem getByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

    }

    public InventoryItem createOrUpdate(Long productId, int quantity) {
        return inventoryRepository.findByProductId(productId)
                .map(existing -> {
                    existing.setQuantity(quantity);
                    return inventoryRepository.save(existing);
                })
                .orElseGet(() -> inventoryRepository.save(new InventoryItem(productId, quantity)));

    }

    public InventoryItem decreaseStock(Long productId, int amount) {
        InventoryItem item = getByProductId(productId);
        item.decreaseQuantity(amount);
        return inventoryRepository.save(item);
    }

    public InventoryItem increaseStock(Long productId, int amount) {
        InventoryItem item = getByProductId(productId);
        item.increaseQuantity(amount);
        return inventoryRepository.save(item);

    }

    public boolean isAvailable(Long productId, int requestedQuantity) {
        InventoryItem item = getByProductId(productId);
        return item.getQuantity() >= requestedQuantity;
    }

    public int getAvailableStock(Long productId) {
        InventoryItem item = getByProductId(productId);

        int reserved = reservationRepository
                .findByProductIdAndStatus(productId, ReservationStatus.RESERVED)
                .stream()
                .mapToInt(r -> r.getQuantity())
                .sum();

        return item.getQuantity() - reserved;
    }

    public StockReservation reserve(Long productId, Long orderId, int quantity) {

        int available = getAvailableStock(productId);

        if (available < quantity) {
            throw new IllegalStateException("Not enough stock available");
        }

        StockReservation reservation = new StockReservation(
                productId,
                orderId,
                quantity,
                LocalDateTime.now().plusMinutes(15)
        );

        return reservationRepository.save(reservation);
    }

    public void releaseByOrder(Long orderId) {
        List<StockReservation> reservations = reservationRepository.findByOrderId(orderId);

        for (StockReservation r : reservations) {
            r.setStatus(ReservationStatus.RELEASED);
        }

        reservationRepository.saveAll(reservations);
    }

    public void confirmByOrder(Long orderId) {
        List<StockReservation> reservations = reservationRepository.findByOrderId(orderId);

        for (StockReservation r : reservations) {
            r.setStatus(ReservationStatus.CONFIRMED);

            InventoryItem item = getByProductId(r.getProductId());
            item.decreaseQuantity(r.getQuantity());
            inventoryRepository.save(item);
        }

        reservationRepository.saveAll(reservations);
    }
}
