package com.winter.inventoryservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stock_reservations")
@Getter
@Setter
public class StockReservation {
 @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private Long productId;

    private Long orderId;

    private Integer quantity;

    @Enumerated(EnumType.STRING)

    private ReservationStatus status;

    private LocalDateTime expiresAt;

    public StockReservation() {}

    public StockReservation(Long productId, Long orderId, Integer quantity, LocalDateTime expiresAt) {

        this.productId = productId;

        this.orderId = orderId;

        this.quantity = quantity;

        this.status = ReservationStatus.RESERVED;

        this.expiresAt = expiresAt;

    }
}
