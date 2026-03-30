package com.onlinepharmacy.order.controller;

import com.onlinepharmacy.order.dto.request.CheckoutStartRequestDto;
import com.onlinepharmacy.order.dto.request.PaymentInitiateRequestDto;
import com.onlinepharmacy.order.dto.response.OrderCheckoutResponseDto;
import com.onlinepharmacy.order.dto.response.OrderPaymentResponseDto;
import com.onlinepharmacy.order.service.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling order checkout and payment flows.
 */
@RestController
@RequestMapping("/api")
public class CheckoutController {
  private final CheckoutService checkoutService;

  public CheckoutController(CheckoutService checkoutService) {
    this.checkoutService = checkoutService;
  }

  /**
   * Initiates the checkout process for the user's current cart.
   *
   * @param authentication the current user's authentication
   * @param req the checkout start request details
   * @return the order checkout response
   */
  @PostMapping("/orders/checkout/start")
  public OrderCheckoutResponseDto checkoutStart(
      Authentication authentication,
      @Valid @RequestBody CheckoutStartRequestDto req
  ) {
    String customerId = authentication.getName();
    return checkoutService.checkoutStart(customerId, req.getAddress());
  }

  /**
   * Initiates the payment process for a specific order.
   *
   * @param authentication the current user's authentication
   * @param req the payment initiation request details
   * @return the order payment response
   */
  @PostMapping("/orders/payments/initiate")
  public OrderPaymentResponseDto initiatePayment(
      Authentication authentication,
      @Valid @RequestBody PaymentInitiateRequestDto req
  ) {
    String customerId = authentication.getName();
    return checkoutService.initiatePayment(customerId, req.getOrderId());
  }
}
