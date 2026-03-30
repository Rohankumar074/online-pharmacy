package com.onlinepharmacy.notification.service;

import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;
import com.onlinepharmacy.notification.client.UserClient;

public class NotificationServiceTest {

  @Test
  public void sendPaymentConfirmation_doesNotThrow() {
    JavaMailSender mailSender = mock(JavaMailSender.class);
    SpringTemplateEngine templateEngine = mock(SpringTemplateEngine.class);
    UserClient userClient = mock(UserClient.class);
    
    NotificationService service = new NotificationService(mailSender, templateEngine, userClient);
    // Since it logs on failure, we just verify no exception is thrown during the process
    service.sendPaymentConfirmation(1L, "1");
  }
}
