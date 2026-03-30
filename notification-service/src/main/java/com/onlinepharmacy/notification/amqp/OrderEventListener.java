package com.onlinepharmacy.notification.amqp;

import com.onlinepharmacy.notification.dto.event.OrderStatusChangedEvent;
import com.onlinepharmacy.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

  private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
  private final NotificationService notificationService;

  public OrderEventListener(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @RabbitListener(queues = "${amqp.queue.notifications:pharmacy.notifications.email}")
  public void handleOrderStatusChanged(OrderStatusChangedEvent event) {
    log.info("Received order status changed event: {}", event);

    if ("PAID".equalsIgnoreCase(event.newStatus())) {
      log.info("Order {} for customer {} is PAID. Triggering email notification...", event.orderId(), event.customerId());
      notificationService.sendPaymentConfirmation(event.orderId(), event.customerId());
    } else {
      log.debug("Ignoring status change to {} for order {}", event.newStatus(), event.orderId());
    }
  }
}
