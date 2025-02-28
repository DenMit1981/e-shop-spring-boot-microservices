package com.denmit.eshop.attachmentservice.service.impl;

import com.denmit.eshop.attachmentservice.client.HistoryClient;
import com.denmit.eshop.attachmentservice.client.OrderClient;
import com.denmit.eshop.attachmentservice.dto.AttachmentNameResponseDto;
import com.denmit.eshop.attachmentservice.dto.AttachmentResponseDto;
import com.denmit.eshop.attachmentservice.dto.OrderUserResponseDto;
import com.denmit.eshop.attachmentservice.exception.FileAlreadyExistsException;
import com.denmit.eshop.attachmentservice.exception.FileNotFoundException;
import com.denmit.eshop.attachmentservice.exception.AttachmentAccessException;
import com.denmit.eshop.attachmentservice.mapper.AttachmentMapper;
import com.denmit.eshop.attachmentservice.model.Attachment;
import com.denmit.eshop.attachmentservice.repository.AttachmentRepository;
import com.denmit.eshop.attachmentservice.service.AttachmentService;
import com.denmit.eshop.attachmentservice.service.MinioService;
import com.denmit.eshop.attachmentservice.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final OrderClient orderClient;
    private final HistoryClient historyClient;
    private final MinioService minioService;
    private final ValidationService validationService;

    @Override
    public AttachmentResponseDto upload(MultipartFile file, Long orderId, Long userId) {
        checkAccess(orderId, userId);

        Attachment attachment = processAttachment(file, orderId, true);

        historyClient.saveHistoryForAttachedFile(attachment.getId(), orderId);

        return attachmentMapper.toDto(attachment);
    }

    @Override
    public List<AttachmentResponseDto> getAllByOrderId(Long orderId) {
        orderClient.checkOrderExistenceById(orderId);

        return attachmentMapper.toDtos(attachmentRepository.findByOrderId(orderId));
    }

    @Override
    public AttachmentNameResponseDto getByIdForHistory(Long fileId) {
        return attachmentMapper.toNameDto(findById(fileId));
    }

    @Override
    public AttachmentResponseDto getInfo(Long fileId) {
        return attachmentMapper.toDto(findById(fileId));
    }

    @Override
    public InputStream download(Long fileId) {
        Attachment attachment = findById(fileId);

        return minioService.downloadFile(attachment.getFilePath());
    }

    @Override
    public AttachmentResponseDto replace(Long attachmentId, MultipartFile file, Long userId) {
        Attachment replacedFile = findById(attachmentId);
        String oldFileName = replacedFile.getFileName();

        checkAccess(replacedFile.getOrderId(), userId);
        processAttachment(file, replacedFile.getOrderId(), false, replacedFile);
        historyClient.saveHistoryForReplacedFile(attachmentId, replacedFile.getOrderId(), oldFileName);

        return attachmentMapper.toDto(replacedFile);
    }

    @Override
    public void deleteById(Long fileId, Long userId) {
        Attachment attachment = findById(fileId);

        checkAccess(attachment.getOrderId(), userId);
        historyClient.saveHistoryForRemovedFile(fileId, attachment.getOrderId());

        log.info("File {} has just been deleted from order {}", attachment.getFileName(), attachment.getOrderId());

        attachmentRepository.deleteById(fileId);
    }

    @Override
    public void deleteByName(String fileName, Long orderId, Long userId) {
        checkAccess(orderId, userId);

        if (!attachmentRepository.existsByOrderIdAndFileName(orderId, fileName)) {
            throw new FileNotFoundException();
        }

        Attachment attachment = attachmentRepository.findByFileNameAndOrderId(fileName, orderId);

        historyClient.saveHistoryForRemovedFileByName(fileName, orderId);

        removeAttachment(attachment);
    }

    @Transactional
    private Attachment processAttachment(MultipartFile file, Long orderId, boolean isNew, Attachment... existingAttachment) {
        orderClient.checkOrderExistenceById(orderId);
        validationService.validateUploadFile(file);

        String fileName = file.getOriginalFilename();
        String filePath = (isNew ? UUID.randomUUID().toString() : existingAttachment[0].getId().toString()) + "." + FilenameUtils.getExtension(fileName);

        if (attachmentRepository.existsByOrderIdAndFileName(orderId, fileName)) {
            throw new FileAlreadyExistsException();
        }

        if (!isNew) {
            minioService.removeFile(existingAttachment[0].getFilePath());
        }

        uploadFile(filePath, file);

        Attachment attachment = isNew
                ? createAttachment(fileName, filePath, file.getSize(), orderId)
                : updateAttachment(existingAttachment[0], fileName, filePath, file.getSize());

        attachmentRepository.save(attachment);

        log.info("{} file {} has just been {} to order {}",
                isNew ? "New" : "Replaced",
                attachment.getFileName(),
                isNew ? "added" : "updated",
                orderId);

        return attachment;
    }

    @Transactional
    private void removeAttachment(Attachment attachment) {
        minioService.removeFile(attachment.getFilePath());

        log.info("File {} has just been deleted from order {}", attachment.getFileName(), attachment.getOrderId());

        attachmentRepository.deleteById(attachment.getId());
    }

    private Attachment findById(Long fileId) {
        return attachmentRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
    }

    private void uploadFile(String filePath, MultipartFile file) {
        try {
            minioService.upload(filePath, file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkAccess(Long orderId, Long userId) {
        OrderUserResponseDto order = orderClient.getOrderUserById(orderId);

        if (!order.getUserId().equals(userId)) {
            throw new AttachmentAccessException();
        }
    }

    private Attachment createAttachment(String fileName, String filePath, long fileSize, Long orderId) {
        Attachment attachment = new Attachment();

        attachment.setFileName(fileName);
        attachment.setFilePath(filePath);
        attachment.setFileSize(fileSize);
        attachment.setOrderId(orderId);

        return attachment;
    }

    private Attachment updateAttachment(Attachment attachment, String fileName, String filePath, long fileSize) {
        attachment.setFileName(fileName);
        attachment.setFilePath(filePath);
        attachment.setFileSize(fileSize);

        return attachment;
    }
}
