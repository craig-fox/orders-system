package com.winter.inventoryservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveStockRequest {
    private Long productId;

    private Long orderId;

    private Integer quantity;

    public ReserveStockRequest() {}
}
