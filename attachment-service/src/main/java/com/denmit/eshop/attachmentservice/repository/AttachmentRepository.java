package com.denmit.eshop.attachmentservice.repository;

import com.denmit.eshop.attachmentservice.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByOrderId(Long orderId);

    boolean existsByOrderIdAndFileName(Long orderId, String fileName);

    Attachment findByFileNameAndOrderId(String fileName, Long orderId);
}
