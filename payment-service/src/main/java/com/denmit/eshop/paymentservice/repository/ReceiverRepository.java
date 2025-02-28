package com.denmit.eshop.paymentservice.repository;

import com.denmit.eshop.paymentservice.model.MerchantPaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiverRepository extends JpaRepository<MerchantPaymentDetails, Long> {
}
