package com.winter.payments.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.winter.payments.domain.Payment;
import com.winter.contracts.PaymentRequest;
import com.winter.contracts.PaymentResponse;
import com.winter.contracts.PaymentStatus;
import com.winter.payments.exception.PaymentException;
import com.winter.payments.exception.PaymentSystemException;
import com.winter.payments.repository.PaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse processPayment(PaymentRequest request) {

        log.info("payment_requested orderId={} amount={}",
                request.orderId(), request.amount());

        validateRequest(request);

        return paymentRepository.findByOrderId(request.orderId())
                .map(this::mapToResponse)
                .orElseGet(() -> createAndSavePayment(request));
    }

    private PaymentResponse createAndSavePayment(PaymentRequest request) {
        simulateInfrastructureFailure();

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setOrderId(request.orderId());
        payment.setAmount(request.amount());
        payment.setCreatedAt(Instant.now());

        payment.setStatus(
                request.amount().compareTo(BigDecimal.valueOf(100)) > 0
                        ? PaymentStatus.FAILED_INSUFFICIENT_FUNDS
                        : PaymentStatus.SUCCESS
        );

        paymentRepository.save(payment);

        return mapToResponse(payment);
    }

    private void validateRequest(PaymentRequest request) {
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentException("Amount must be greater than zero");
        }
    }


    private void simulateInfrastructureFailure() {
        if (Math.random() < 0.05) {
            throw new PaymentSystemException("Payment processor unavailable");
        }
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId().toString(),
                payment.getStatus().name()
        );
    }
}
