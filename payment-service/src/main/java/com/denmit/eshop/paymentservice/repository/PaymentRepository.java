package com.denmit.eshop.paymentservice.repository;

import com.denmit.eshop.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByOrderId(Long orderId);

    Optional<Payment> findByOrderId(Long orderId);

    Optional<Payment> findByPaymentNumber(String paymentNumber);
}
