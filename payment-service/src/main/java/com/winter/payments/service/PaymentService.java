package com.winter.payments.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.winter.payments.domain.Payment;
import com.winter.payments.domain.PaymentStatus;
import com.winter.payments.dto.PaymentRequest;
import com.winter.payments.dto.PaymentResponse;
import com.winter.payments.exception.PaymentException;
import com.winter.payments.repository.PaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    
    public PaymentResponse processPayment(PaymentRequest request) {

        log.info("payment_requested orderId={} amount={}",
                request.orderId(), request.amount());

        // 🔑 1. Check for existing payment FIRST
        Optional<Payment> existing = paymentRepository.findByOrderId(request.orderId());

        if (existing.isPresent()) {
            Payment payment = existing.get();

            log.info("payment_already_exists orderId={} paymentId={} status={}",
                    payment.getOrderId(), payment.getId(), payment.getStatus());

            return new PaymentResponse(
                    payment.getId().toString(),
                    payment.getStatus().name()
            );
        }

        // 2. Create new payment
        UUID paymentId = UUID.randomUUID();
        PaymentStatus status;

        try {
            if (request.amount().compareTo(BigDecimal.valueOf(100)) > 0) {
                status = PaymentStatus.DECLINED;
            } else if (Math.random() < 0.3) {
                throw new PaymentException("Random payment failure");
            } else {
                status = PaymentStatus.APPROVED;
            }

        } catch (PaymentException ex) {
            log.warn("payment_failed orderId={} reason={}", request.orderId(), ex.getMessage());
            status = PaymentStatus.DECLINED;
        }

        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setOrderId(request.orderId());
        payment.setAmount(request.amount());
        payment.setStatus(status);
        payment.setCreatedAt(Instant.now());

        paymentRepository.save(payment);

        log.info("payment_result orderId={} paymentId={} status={}",
                request.orderId(), paymentId, status);

        return new PaymentResponse(paymentId.toString(), status.name());
    }   
}
