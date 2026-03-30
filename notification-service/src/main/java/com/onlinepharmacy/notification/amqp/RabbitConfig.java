package com.onlinepharmacy.notification.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  @Value("${amqp.exchange.orders:pharmacy.orders}")
  private String ordersExchangeName;

  @Value("${amqp.queue.notifications:pharmacy.notifications.email}")
  private String notificationsQueueName;

  @Bean
  public TopicExchange ordersExchange() {
    return new TopicExchange(ordersExchangeName, true, false);
  }

  @Bean
  public Queue notificationsQueue() {
    return new Queue(notificationsQueueName, true);
  }

  @Bean
  public Binding notificationsBinding(Queue notificationsQueue, TopicExchange ordersExchange) {
    return BindingBuilder.bind(notificationsQueue).to(ordersExchange).with("order.status.changed");
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
