package com.winter.payments.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.winter.payments.exception.PaymentSystemException;
import com.winter.payments.exception.PaymentValidationException;

public class PaymentExceptionHandler {
   @ExceptionHandler(PaymentValidationException.class)
    public ResponseEntity<String> handleValidation(PaymentValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PaymentSystemException.class)

    public ResponseEntity<String> handleSystem(PaymentSystemException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Payment service temporarily unavailable");
    }
}
