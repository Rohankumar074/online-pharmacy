package com.onlinepharmacy.order.amqp;

import com.onlinepharmacy.order.dto.event.OrderStatusChangedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final String exchange;

  public OrderEventPublisher(
      RabbitTemplate rabbitTemplate,
      @Value("${amqp.exchange.orders:pharmacy.orders}") String exchange
  ) {
    this.rabbitTemplate = rabbitTemplate;
    this.exchange = exchange;
  }

  public void publishStatusChanged(Long orderId, String customerId, String newStatus) {
    rabbitTemplate.convertAndSend(exchange, "order.status.changed",
        new OrderStatusChangedEvent(orderId, customerId, newStatus));
  }
}
