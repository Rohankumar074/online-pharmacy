package com.onlinepharmacy.order.service;

import com.onlinepharmacy.order.client.CatalogClient;
import com.onlinepharmacy.order.dto.response.OrderCartResponseDto;
import com.onlinepharmacy.order.exception.ConflictException;
import com.onlinepharmacy.order.model.Order;
import com.onlinepharmacy.order.model.OrderStatus;
import com.onlinepharmacy.order.repo.OrderRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CartServiceTest {

  @Test
  public void addToCart_whenQuantityNonPositive_throws() {
    OrderRepository repo = Mockito.mock(OrderRepository.class);
    CatalogClient catalog = Mockito.mock(CatalogClient.class);
    CartService svc = new CartService(repo, catalog);

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> svc.addToCart("1", 10L, 0)
    );
  }

  @Test
  public void addToCart_whenStockEnough_addsItemAndTotal() {
    OrderRepository repo = Mockito.mock(OrderRepository.class);
    CatalogClient catalog = Mockito.mock(CatalogClient.class);

    Mockito.when(repo.findTopByCustomerIdAndStatus("1", OrderStatus.DRAFT_CART))
        .thenReturn(Optional.empty());
    Mockito.when(repo.save(Mockito.any(Order.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    Mockito.when(catalog.getMedicine(10L)).thenReturn(new CatalogClient.CatalogMedicineDto(
        10L,
        "Aspirin",
        "Pain",
        new BigDecimal("10.50"),
        false,
        100
    ));

    CartService svc = new CartService(repo, catalog);
    OrderCartResponseDto cart = svc.addToCart("1", 10L, 2);

    Assertions.assertEquals(OrderStatus.DRAFT_CART, cart.status());
    Assertions.assertEquals(2, cart.items().get(0).quantity());
    Assertions.assertEquals(0, cart.totalAmount().compareTo(new BigDecimal("21.00")));
  }

  @Test
  public void addToCart_whenStockNotEnough_throws() {
    OrderRepository repo = Mockito.mock(OrderRepository.class);
    CatalogClient catalog = Mockito.mock(CatalogClient.class);

    Mockito.when(repo.findTopByCustomerIdAndStatus("1", OrderStatus.DRAFT_CART))
        .thenReturn(Optional.empty());
    Mockito.when(repo.save(Mockito.any(Order.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    Mockito.when(catalog.getMedicine(10L)).thenReturn(new CatalogClient.CatalogMedicineDto(
        10L,
        "Aspirin",
        "Pain",
        new BigDecimal("10.50"),
        false,
        1
    ));

    CartService svc = new CartService(repo, catalog);

    Assertions.assertThrows(ConflictException.class,
        () -> svc.addToCart("1", 10L, 2)
    );
  }
}
