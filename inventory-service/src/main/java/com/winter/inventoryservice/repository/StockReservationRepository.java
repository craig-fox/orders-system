package com.winter.inventoryservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.winter.inventoryservice.model.ReservationStatus;
import com.winter.inventoryservice.model.StockReservation;

public interface StockReservationRepository extends JpaRepository<StockReservation, Long> {
    List<StockReservation> findByProductIdAndStatus(Long productId, ReservationStatus status);
    List<StockReservation> findByOrderId(Long orderId);
}
