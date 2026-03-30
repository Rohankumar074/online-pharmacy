package com.onlinepharmacy.order.controller;

import com.onlinepharmacy.order.dto.request.AddToCartRequestDto;
import com.onlinepharmacy.order.dto.request.UpdateCartItemRequestDto;
import com.onlinepharmacy.order.dto.response.OrderCartResponseDto;
import com.onlinepharmacy.order.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing user shopping carts.
 */
@RestController
@RequestMapping("/api/orders")
public class CartController {
  private final CartService cartService;

  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  /**
   * Adds a medicine to the user's cart.
   *
   * @param authentication the current user's authentication
   * @param req the request containing medicine ID and quantity
   * @return the updated cart response
   */
  @PostMapping("/cart")
  public OrderCartResponseDto addToCart(Authentication authentication, @Valid @RequestBody AddToCartRequestDto req) {
    String customerId = authentication.getName();
    return cartService.addToCart(customerId, req.getMedicineId(), req.getQuantity());
  }

  /**
   * Retrieves the current user's cart details.
   *
   * @param authentication the current user's authentication
   * @return the user's cart response
   */
  @GetMapping("/cart")
  public OrderCartResponseDto getCart(Authentication authentication) {
    String customerId = authentication.getName();
    return cartService.getCart(customerId);
  }

  /**
   * Updates the quantity of a specific item in the cart.
   *
   * @param authentication the current user's authentication
   * @param req the request containing medicine ID and new quantity
   * @return the updated cart response
   */
  @PutMapping("/cart/item")
  public OrderCartResponseDto updateCartItem(Authentication authentication, @Valid @RequestBody UpdateCartItemRequestDto req) {
    String customerId = authentication.getName();
    return cartService.updateQuantity(customerId, req.getMedicineId(), req.getQuantity());
  }

  /**
   * Toggles the substitution preference for the user's cart.
   *
   * @param authentication the current user's authentication
   * @param enabled whether substitution should be enabled
   * @return the updated cart response
   */
  @PutMapping("/cart/substitution")
  public OrderCartResponseDto toggleSubstitution(Authentication authentication, @RequestParam("enabled") boolean enabled) {
    String customerId = authentication.getName();
    return cartService.toggleSubstitution(customerId, enabled);
  }
}
