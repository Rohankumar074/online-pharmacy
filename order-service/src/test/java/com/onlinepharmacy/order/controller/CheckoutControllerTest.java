package com.onlinepharmacy.order.controller;

import com.onlinepharmacy.order.dto.AddressDto;
import com.onlinepharmacy.order.dto.response.OrderCheckoutResponseDto;
import com.onlinepharmacy.order.dto.response.OrderPaymentResponseDto;
import com.onlinepharmacy.order.model.OrderStatus;
import com.onlinepharmacy.order.service.CheckoutService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckoutController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CheckoutControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CheckoutService checkoutService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser(username = "user1")
  public void checkoutStart_returns200() throws Exception {
    AddressDto addr = new AddressDto("123 St", "City", "12345", "State");
    Mockito.when(checkoutService.checkoutStart(Mockito.anyString(), Mockito.any()))
        .thenReturn(new OrderCheckoutResponseDto(1L, OrderStatus.PAYMENT_PENDING));

    mockMvc.perform(post("/api/orders/checkout/start")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(addr)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user1")
  public void initiatePayment_returns200() throws Exception {
    Mockito.when(checkoutService.initiatePayment(Mockito.anyString(), Mockito.anyLong()))
        .thenReturn(new OrderPaymentResponseDto(1L, OrderStatus.PAID, "STUB"));

    mockMvc.perform(post("/api/orders/payments/initiate")
        .param("orderId", "1"))
        .andExpect(status().isOk());
  }
}
