package com.onlinepharmacy.order.service;

import com.onlinepharmacy.order.amqp.OrderEventPublisher;
import com.onlinepharmacy.order.client.CatalogClient;
import com.onlinepharmacy.order.dto.AddressDto;
import com.onlinepharmacy.order.dto.response.OrderCheckoutResponseDto;
import com.onlinepharmacy.order.dto.response.OrderPaymentResponseDto;
import com.onlinepharmacy.order.exception.ConflictException;
import com.onlinepharmacy.order.model.Order;
import com.onlinepharmacy.order.model.OrderItem;
import com.onlinepharmacy.order.model.OrderStatus;
import com.onlinepharmacy.order.repo.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CheckoutServiceTest {

  private CheckoutService checkoutService;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private CatalogClient catalogClient;

  @Mock
  private OrderEventPublisher orderEventPublisher;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    checkoutService = new CheckoutService(orderRepository, catalogClient, orderEventPublisher);
  }

  @Test
  public void checkoutStart_whenNoRxNeeded_setsPaymentPending() {
    Order order = new Order();
    order.setId(1L);
    order.setCustomerId("user1");
    OrderItem item = new OrderItem();
    item.setMedicineId(10L);
    order.setItems(List.of(item));

    when(orderRepository.findTopByCustomerIdAndStatus("user1", OrderStatus.DRAFT_CART))
        .thenReturn(Optional.of(order));
    
    CatalogClient.CatalogMedicineDto medDto = new CatalogClient.CatalogMedicineDto(10L, "Med", "Cat", null, false, 100);
    when(catalogClient.getMedicine(10L)).thenReturn(medDto);

    AddressDto addr = new AddressDto("123 St", "City", "12345", "State");
    OrderCheckoutResponseDto result = checkoutService.checkoutStart("user1", addr);

    Assertions.assertEquals(OrderStatus.PAYMENT_PENDING, result.status());
    verify(orderRepository).save(order);
    verify(orderEventPublisher).publishStatusChanged(eq(1L), eq("user1"), eq("PAYMENT_PENDING"));
  }

  @Test
  public void checkoutStart_whenRxNeeded_setsPrescriptionPending() {
    Order order = new Order();
    order.setId(1L);
    order.setCustomerId("user1");
    OrderItem item = new OrderItem();
    item.setMedicineId(10L);
    order.setItems(List.of(item));

    when(orderRepository.findTopByCustomerIdAndStatus("user1", OrderStatus.DRAFT_CART))
        .thenReturn(Optional.of(order));
    
    CatalogClient.CatalogMedicineDto medDto = new CatalogClient.CatalogMedicineDto(10L, "Med", "Cat", null, true, 100);
    when(catalogClient.getMedicine(10L)).thenReturn(medDto);

    AddressDto addr = new AddressDto("123 St", "City", "12345", "State");
    OrderCheckoutResponseDto result = checkoutService.checkoutStart("user1", addr);

    Assertions.assertEquals(OrderStatus.PRESCRIPTION_PENDING, result.status());
    verify(orderRepository).save(order);
    verify(orderEventPublisher).publishStatusChanged(eq(1L), eq("user1"), eq("PRESCRIPTION_PENDING"));
  }

  @Test
  public void initiatePayment_whenValidState_setsPaid() {
    Order order = new Order();
    order.setId(1L);
    order.setCustomerId("user1");
    order.setStatus(OrderStatus.PAYMENT_PENDING);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    OrderPaymentResponseDto result = checkoutService.initiatePayment("user1", 1L);

    Assertions.assertEquals(OrderStatus.PAID, result.status());
    verify(orderRepository).save(order);
    verify(orderEventPublisher).publishStatusChanged(eq(1L), eq("user1"), eq("PAID"));
  }

  @Test
  public void initiatePayment_whenInvalidState_throwsConflict() {
    Order order = new Order();
    order.setId(1L);
    order.setCustomerId("user1");
    order.setStatus(OrderStatus.PRESCRIPTION_PENDING);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    Assertions.assertThrows(ConflictException.class, () -> checkoutService.initiatePayment("user1", 1L));
  }
}
