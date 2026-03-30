package com.onlinepharmacy.identity.service;

import com.onlinepharmacy.identity.dto.response.AuthResponseDto;
import com.onlinepharmacy.identity.dto.request.LoginRequestDto;
import com.onlinepharmacy.identity.dto.request.SignupRequestDto;
import com.onlinepharmacy.identity.exception.ConflictException;
import com.onlinepharmacy.identity.exception.ResourceNotFoundException;
import com.onlinepharmacy.identity.exception.UnauthorizedException;
import com.onlinepharmacy.identity.mapper.IdentityMapper;
import com.onlinepharmacy.identity.model.User;
import com.onlinepharmacy.identity.repository.UserRepository;
import com.onlinepharmacy.identity.security.JwtTokenService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenService jwtTokenService;

  public AuthService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtTokenService jwtTokenService
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenService = jwtTokenService;
  }

  @Transactional
  public AuthResponseDto signup(SignupRequestDto data) {
    userRepository.findByEmail(data.getEmail())
        .ifPresent(u -> {
          throw new ConflictException("Email already registered");
        });

    User user = IdentityMapper.toEntity(data, passwordEncoder);

    user = userRepository.save(user);
    return toAuthResponse(user);
  }

  public AuthResponseDto login(LoginRequestDto data) {
    User user = userRepository.findByEmail(data.getEmail().toLowerCase())
        .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

    if (!user.isEnabled() || user.getPasswordHash() == null) {
      throw new UnauthorizedException("Invalid credentials");
    }

    if (!passwordEncoder.matches(data.getPassword(), user.getPasswordHash())) {
      throw new UnauthorizedException("Invalid credentials");
    }

    return toAuthResponse(user);
  }

  public User getUserOrThrow(String userId) {
    long id;
    try {
      id = Long.parseLong(userId);
    } catch (NumberFormatException ex) {
      throw new UnauthorizedException("Invalid user id in headers");
    }

    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private AuthResponseDto toAuthResponse(User user) {
    String userId = String.valueOf(user.getId());
    String token = jwtTokenService.generateToken(userId, user.rolesAsList());
    return IdentityMapper.toAuthResponseDto(user, token);
  }
}
