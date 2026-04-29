package com.winter.payments.exception;

public class PaymentValidationException extends RuntimeException {
   public PaymentValidationException(String msg) {
        super(msg);
    }

}
