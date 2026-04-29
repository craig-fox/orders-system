package com.winter.contracts;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PaymentRequest( 
    @NotBlank(message = "orderId must not be blank")
    UUID orderId,

    @Positive(message = "amount must be greater than 0")
    BigDecimal amount,
    String currency

) {}


