package com.denmit.eshop.feedbackservice.repository;

import com.denmit.eshop.feedbackservice.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findByOrderId(Long orderId, Pageable pageable);

    List<Feedback> findByOrderIdOrderByDate(Long orderId);
}
