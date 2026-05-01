package com.winter.contracts;

public enum PaymentStatus {
    SUCCESS,
    PENDING,
    FAILED_INSUFFICIENT_FUNDS,
    FAILED_VALIDATION,
    FAILED_INTERNAL_ERROR
}
