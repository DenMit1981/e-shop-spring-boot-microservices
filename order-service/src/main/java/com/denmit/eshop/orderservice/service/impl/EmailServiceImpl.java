package com.denmit.eshop.orderservice.service.impl;

import com.denmit.eshop.orderservice.client.FeedbackClient;
import com.denmit.eshop.orderservice.client.UserClient;
import com.denmit.eshop.orderservice.dto.response.FeedbackMessageResponseDto;
import com.denmit.eshop.orderservice.dto.response.UserResponseDto;
import com.denmit.eshop.orderservice.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String EMPTY_STRING = "";
    private static final String HEADER = "Your";
    private static final String FOOTER = "Thanks for your choice";
    private static final String ORDER_DETAILS_SUBJECT = "Order details";
    private static final String ORDER_DETAILS_TEMPLATE = "orderDetails.html";

    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final JavaMailSender emailSender;
    private final UserClient userClient;
    private final FeedbackClient feedbackClient;

    @Value("${mail.username}")
    private String sendFrom;

    @Override
    public void sendOrderDetailsMessage(Long orderId, String order, Long userId) {
        List<UserResponseDto> admins = userClient.getAllAdmins();
        UserResponseDto buyer = userClient.getById(userId);

        admins.forEach(recipient -> sendMessageUsingThymeleafTemplate(recipient.getEmail(),
                ORDER_DETAILS_SUBJECT,
                getTemplateModelForOrder(orderId, order, "Admins", EMPTY_STRING, EMPTY_STRING),
                ORDER_DETAILS_TEMPLATE));

        sendMessageUsingThymeleafTemplate(buyer.getEmail(),
                ORDER_DETAILS_SUBJECT,
                getTemplateModelForOrder(orderId, order, buyer.getName(), HEADER, FOOTER),
                ORDER_DETAILS_TEMPLATE);
    }

    @Override
    public void sendFeedbackMessage(Long orderId, Long feedbackId, Long userId) {
        List<UserResponseDto> admins = userClient.getAllAdmins();
        UserResponseDto buyer = userClient.getById(userId);
        FeedbackMessageResponseDto feedback = feedbackClient.getByIdForEmailMessage(feedbackId);

        admins.forEach(recipient -> sendMessageUsingThymeleafTemplate(recipient.getEmail(),
                "Feedback was provided",
                Map.of(
                        "orderId", orderId,
                        "login", buyer.getName(),
                        "feedbackRate", feedback.getRate(),
                        "feedbackComment", feedback.getText()
                ), "orderFeedback.html"));
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

    private Map<String, Object> getTemplateModelForOrder(Long orderId, String order, String login,
                                                         String header, String footer) {
        return Map.of(
                "orderId", orderId,
                "login", login,
                "order", order,
                "header", header,
                "footer", footer
        );
    }
}
