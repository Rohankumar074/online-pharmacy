package com.onlinepharmacy.order.controller;

import com.onlinepharmacy.order.dto.request.AddToCartRequestDto;
import com.onlinepharmacy.order.dto.request.UpdateCartItemRequestDto;
import com.onlinepharmacy.order.dto.response.OrderCartResponseDto;
import com.onlinepharmacy.order.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CartControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CartService cartService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser(username = "user1")
  public void addToCart_returns200() throws Exception {
    AddToCartRequestDto req = new AddToCartRequestDto();
    req.setMedicineId(1L);
    req.setQuantity(2);

    Mockito.when(cartService.addToCart(Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt()))
        .thenReturn(new OrderCartResponseDto(1L, null, Collections.emptyList(), BigDecimal.TEN));

    mockMvc.perform(post("/api/orders/cart")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user1")
  public void getCart_returns200() throws Exception {
    mockMvc.perform(get("/api/orders/cart"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user1")
  public void updateCartItem_returns200() throws Exception {
    UpdateCartItemRequestDto req = new UpdateCartItemRequestDto();
    req.setMedicineId(1L);
    req.setQuantity(5);

    mockMvc.perform(put("/api/orders/cart/item")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk());
  }
}
