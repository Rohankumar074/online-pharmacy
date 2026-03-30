package com.onlinepharmacy.order.service;

import com.onlinepharmacy.order.amqp.OrderEventPublisher;
import com.onlinepharmacy.order.dto.response.OrderDetailsResponseDto;
import com.onlinepharmacy.order.dto.response.SalesReportResponseDto;
import com.onlinepharmacy.order.exception.ForbiddenException;
import com.onlinepharmacy.order.model.Order;
import com.onlinepharmacy.order.model.OrderStatus;
import com.onlinepharmacy.order.repo.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class OrderServiceTest {

  private OrderService orderService;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private OrderEventPublisher orderEventPublisher;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    orderService = new OrderService(orderRepository, orderEventPublisher);
  }

  @Test
  public void getOrder_whenExistsAndOwner_returnsDto() {
    Order order = new Order();
    order.setId(1L);
    order.setCustomerId("user1");
    order.setStatus(OrderStatus.PAID);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    OrderDetailsResponseDto result = orderService.getOrder("user1", 1L);
    Assertions.assertEquals(1L, result.id());
  }

  @Test
  public void getOrder_whenNotOwner_throwsForbidden() {
    Order order = new Order();
    order.setId(1L);
    order.setCustomerId("other_user");

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    Assertions.assertThrows(ForbiddenException.class, () -> orderService.getOrder("user1", 1L));
  }

  @Test
  public void adminUpdateStatus_updatesAndPublishesEvent() {
    Order order = new Order();
    order.setId(1L);
    order.setCustomerId("user1");
    order.setStatus(OrderStatus.PAYMENT_PENDING);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    orderService.adminUpdateStatus(1L, OrderStatus.PAID);

    Assertions.assertEquals(OrderStatus.PAID, order.getStatus());
    verify(orderRepository).save(order);
    verify(orderEventPublisher).publishStatusChanged(eq(1L), eq("user1"), eq("PAID"));
  }

  @Test
  public void handlePrescriptionUpdate_whenOrderExists_updatesStatus() {
    Order order = new Order();
    order.setId(1L);
    order.setCustomerId("user1");
    order.setStatus(OrderStatus.PRESCRIPTION_PENDING);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    orderService.onPrescriptionApproved(1L);

    Assertions.assertEquals(OrderStatus.PAYMENT_PENDING, order.getStatus());
    verify(orderRepository).save(order);
  }

  @Test
  public void getSalesReport_calculatesTotalsCorrectly() {
    Order o1 = new Order();
    o1.setStatus(OrderStatus.PAID);
    o1.setTotalAmount(new BigDecimal("100.00"));

    Order o2 = new Order();
    o2.setStatus(OrderStatus.DELIVERED);
    o2.setTotalAmount(new BigDecimal("50.00"));

    when(orderRepository.findByStatus(OrderStatus.PAID)).thenReturn(List.of(o1));
    when(orderRepository.findByStatus(OrderStatus.DELIVERED)).thenReturn(List.of(o2));

    SalesReportResponseDto result = orderService.getSalesReport();

    Assertions.assertEquals(2, result.totalOrders());
    Assertions.assertEquals(0, result.totalRevenue().compareTo(new BigDecimal("150.00")));
  }
}
