package com.denmit.eshop.commentservice.repository;

import com.denmit.eshop.commentservice.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByOrderId(Long orderId, Pageable pageable);

    List<Comment> findByOrderIdOrderByDate(Long orderId);
}
