package com.denmit.eshop.paymentservice.repository;

import com.denmit.eshop.paymentservice.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByReceipt_OrderId(Long userId);
}
