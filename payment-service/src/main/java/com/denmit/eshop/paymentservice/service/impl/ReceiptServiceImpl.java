package com.denmit.eshop.paymentservice.service.impl;

import com.denmit.eshop.paymentservice.client.UserClient;
import com.denmit.eshop.paymentservice.dto.response.PaymentResponseDto;
import com.denmit.eshop.paymentservice.dto.response.ReceiptResponseDto;
import com.denmit.eshop.paymentservice.exception.PaymentNotFoundException;
import com.denmit.eshop.paymentservice.exception.ReceiptNotFoundException;
import com.denmit.eshop.paymentservice.mapper.PaymentMapper;
import com.denmit.eshop.paymentservice.mapper.ReceiptMapper;
import com.denmit.eshop.paymentservice.model.File;
import com.denmit.eshop.paymentservice.model.Payment;
import com.denmit.eshop.paymentservice.model.Receipt;
import com.denmit.eshop.paymentservice.repository.FileRepository;
import com.denmit.eshop.paymentservice.repository.PaymentRepository;
import com.denmit.eshop.paymentservice.repository.ReceiptRepository;
import com.denmit.eshop.paymentservice.service.MinioService;
import com.denmit.eshop.paymentservice.service.ReceiptService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final FileRepository fileRepository;
    private final PaymentRepository paymentRepository;
    private final MinioService minioService;
    private final ReceiptMapper receiptMapper;
    private final PaymentMapper paymentMapper;
    private final UserClient userClient;

    @Transactional
    @Override
    public ReceiptResponseDto generate(Long userId, Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(PaymentNotFoundException::new);

        if (payment == null) {
            throw new PaymentNotFoundException();
        }

        Receipt receipt = new Receipt();
        receipt.setUserId(userId);
        receipt.setOrderId(orderId);
        receipt.setPayment(payment);

        receiptRepository.save(receipt);

        String filePath = upload(receipt);

        File file = new File();
        file.setName("Receipt_" + orderId + ".pdf");
        file.setPathFile(filePath);
        file.setReceipt(receipt);

        fileRepository.save(file);
        receipt.setFile(file);

        return getReceiptDtoFromReceipt(receipt, filePath);
    }

    @Override
    public ByteArrayResource downloadReceiptFile(Long orderId) {
        File file = fileRepository.findByReceipt_OrderId(orderId).orElseThrow(ReceiptNotFoundException::new);

        try (InputStream inputStream = minioService.download(file.getPathFile())) {
            byte[] byt = inputStream.readAllBytes();
            return new ByteArrayResource(byt);
        } catch (IOException e) {
            throw new RuntimeException("Error downloading file", e);
        }
    }

    @Override
    public ReceiptResponseDto getById(Long receiptId) {
        Receipt receipt = findById(receiptId);
        return getReceiptDtoFromReceipt(receipt, receipt.getFile().getPathFile());
    }

    private String upload(Receipt receipt) {
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream(); // Generate PDF file content

        try {
            Document document = new Document(); // Initialize the PDF document
            PdfWriter writer = PdfWriter.getInstance(document, pdfOutputStream);

            document.open(); // Open the document for writing

            Payment payment = receipt.getPayment();
            String payer = userClient.getById(payment.getUserId()).getName();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String date = payment.getDate().format(formatter);

            document.add(new Paragraph("Receipt № " + receipt.getId()));
            document.add(new Paragraph("Payment № " + payment.getPaymentNumber()));
            document.add(new Paragraph("Order № " + payment.getOrderId()));
            document.add(new Paragraph("Date: " + date));
            document.add(new Paragraph("Amount: " + payment.getAmount()));
            document.add(new Paragraph("Payer: " + payer));
            document.add(new Paragraph("Status: " + payment.getStatus()));
            document.add(new Paragraph("Payment method: " + payment.getPaymentMethod()));
            document.add(new Paragraph("Thank you for your payment!"));

            document.close();

            String fileName = "receipt_" + receipt.getId() + ".pdf"; // Upload the generated PDF to Minio
            minioService.upload(fileName, new ByteArrayInputStream(pdfOutputStream.toByteArray()));

            return fileName; // Return the file name after uploading
        } catch (Exception e) {
            log.error("Error generating and uploading PDF", e);
            throw new RuntimeException("Error generating and uploading PDF", e);
        }
    }

    private Receipt findById(Long receiptId) {
        return receiptRepository.findById(receiptId).orElseThrow(ReceiptNotFoundException::new);
    }

    private ReceiptResponseDto getReceiptDtoFromReceipt(Receipt receipt, String filePath) {
        Payment payment = receipt.getPayment();
        String payer = userClient.getById(payment.getUserId()).getName();
        PaymentResponseDto paymentResponseDto = paymentMapper.toPaymentDto(payment, payer);

        return receiptMapper.toDto(receipt, paymentResponseDto, filePath);
    }
}