package com.denmit.eshop.historyservice.repository;

import com.denmit.eshop.historyservice.model.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    Page<History> findByOrderId(Long orderId, Pageable pageable);

    List<History> findByOrderIdOrderByDate(Long orderId);
}
