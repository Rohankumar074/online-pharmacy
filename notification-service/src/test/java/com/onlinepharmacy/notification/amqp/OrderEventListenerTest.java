package com.onlinepharmacy.notification.amqp;

import com.onlinepharmacy.notification.dto.event.OrderStatusChangedEvent;
import com.onlinepharmacy.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class OrderEventListenerTest {

  private OrderEventListener listener;

  @Mock
  private NotificationService notificationService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    listener = new OrderEventListener(notificationService);
  }

  @Test
  public void handleOrderStatusChanged_whenPaid_callsService() {
    OrderStatusChangedEvent event = new OrderStatusChangedEvent(1L, "user1", "PAID");
    listener.handleOrderStatusChanged(event);
    verify(notificationService).sendPaymentConfirmation(1L, "user1");
  }

  @Test
  public void handleOrderStatusChanged_whenNotPaid_ignores() {
    OrderStatusChangedEvent event = new OrderStatusChangedEvent(1L, "user1", "PENDING");
    listener.handleOrderStatusChanged(event);
    verifyNoInteractions(notificationService);
  }
}
