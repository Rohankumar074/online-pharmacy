package com.onlinepharmacy.identity.controller;

import com.onlinepharmacy.identity.dto.response.AuthResponseDto;
import com.onlinepharmacy.identity.dto.request.LoginRequestDto;
import com.onlinepharmacy.identity.dto.request.SignupRequestDto;
import com.onlinepharmacy.identity.dto.response.ValidatedTokenDto;
import com.onlinepharmacy.identity.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * REST Controller for managing user authentication and registration.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService; 

  public AuthController(AuthService authService) {
    super();
    this.authService = authService;
  }

  /**
   * Registers a new user.
   *
   * @param req the signup request details
   * @return the authentication response containing user info and token
   */
  @PostMapping("/signup")
  public AuthResponseDto signup(@Valid @RequestBody SignupRequestDto req) {
    return authService.signup(req);
  }

  /**
   * Authenticates an existing user and generates a token.
   *
   * @param req the login request containing credentials
   * @return the authentication response containing user info and token
   */
  @PostMapping("/login")
  public AuthResponseDto login(@Valid @RequestBody LoginRequestDto req) {
    return authService.login(req);
  }

  /**
   * Validates the currently authenticated session and retrieves token details.
   *
   * @param authentication the current Spring Security authentication token
   * @return the validated token details including user ID and roles
   */
  @GetMapping("/validate")
  public ValidatedTokenDto validate(Authentication authentication) {
    if (authentication == null || authentication.getPrincipal() == null) {
      throw new UsernameNotFoundException("Unauthenticated");
    }
    String userId = authentication.getPrincipal().toString();
    List<String> roles = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();
    return new ValidatedTokenDto(userId, roles);
  }
}
