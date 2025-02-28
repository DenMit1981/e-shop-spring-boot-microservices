package com.denmit.eshop.paymentservice.repository;

import com.denmit.eshop.paymentservice.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
