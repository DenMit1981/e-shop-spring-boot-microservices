package com.denmit.eshop.paymentservice.service.impl;

import com.denmit.eshop.paymentservice.client.UserClient;
import com.denmit.eshop.paymentservice.dto.response.ReceiptResponseDto;
import com.denmit.eshop.paymentservice.dto.response.UserResponseDto;
import com.denmit.eshop.paymentservice.service.EmailService;
import com.denmit.eshop.paymentservice.service.ReceiptService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final JavaMailSender emailSender;
    private final UserClient userClient;
    private final ReceiptService receiptService;

    @Value("${mail.username}")
    private String sendFrom;

    @Value("${custom.receipt-url}")
    private String receiptUrl;

    @Override
    public void sendReceiptMessage(Long orderId, Long receiptId, Long userId) {
        UserResponseDto buyer = userClient.getById(userId);
        ReceiptResponseDto receipt = receiptService.getById(receiptId);

        sendMessageUsingThymeleafTemplate(buyer.getEmail(),
                "Order was paid successfully",
                Map.of(
                        "orderId", orderId,
                        "login", buyer.getName(),
                        "receipt", receipt.getId(),
                        "fileName", receipt.getFilePath(),
                        "receiptUrl", receiptUrl + orderId
                ), "orderReceipt.html");
    }

    private void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel,
                                                   String template) {
        Context thymeleafContext = new Context();

        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    @SneakyThrows
    private void sendHtmlMessage(String to, String subject, String htmlBody) {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(sendFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        emailSender.send(message);
    }
}
