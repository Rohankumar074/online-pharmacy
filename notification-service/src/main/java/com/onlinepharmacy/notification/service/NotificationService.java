package com.onlinepharmacy.notification.service;

import com.onlinepharmacy.notification.client.UserClient;
import com.onlinepharmacy.notification.dto.UserDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class NotificationService {

  private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;
  private final UserClient userClient;

  @org.springframework.beans.factory.annotation.Value("${spring.mail.username:no-reply@onlinepharmacy.com}")
  private String senderEmail;

  public NotificationService(JavaMailSender mailSender, 
                             SpringTemplateEngine templateEngine,
                             UserClient userClient) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
    this.userClient = userClient;
  }

  @jakarta.annotation.PostConstruct
  public void init() {
    log.info("Notification Service initialized. SMTP Sender Email: {}", senderEmail);
  }

  public void sendPaymentConfirmation(Long orderId, String customerId) {
    try {
      log.info("Fetching user details for customer ID: {}", customerId);
      UserDto user = userClient.getUserById(Long.valueOf(customerId));
      
      String recipientEmail = user.getEmail();
      String customerName = user.getName();

      log.info("Sending payment confirmation email from: {} to: {}", senderEmail, recipientEmail);

      Context context = new Context();
      context.setVariable("customerName", customerName);
      context.setVariable("orderId", orderId);

      String process = templateEngine.process("payment-confirmation", context);

      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setSubject("Payment Confirmation - Order #" + orderId);
      helper.setText(process, true); // true indicates html
      helper.setTo(recipientEmail);
      helper.setFrom(senderEmail);

      mailSender.send(mimeMessage);
      log.info("Successfully sent payment confirmation email for Order #{}", orderId);

    } catch (Exception e) {
      log.error("Failed to send email for Order #{}. Falling back to simulation log. Error: {}", orderId, e.getMessage());
      simulateEmailLog(orderId, customerId);
    }
  }

  private void simulateEmailLog(Long orderId, String customerId) {
    String message = String.format("""
        **********************************************************
        FALLBACK SUBJECT: Payment Confirmation for Order #%d
        TO CUSTOMER ID: %s
        
        Dear Customer,
        
        Your payment for Order #%d has been successfully processed.
        Your order is now being prepared for shipment.
        
        Thank you for shopping with us!
        **********************************************************
        """, orderId, customerId, orderId);

    log.info("SIMULATED email content for Order #{} to customer {}:\n{}", orderId, customerId, message);
  }
}
