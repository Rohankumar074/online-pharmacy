package com.onlinepharmacy.identity.service;

import com.onlinepharmacy.identity.config.JwtProperties;
import com.onlinepharmacy.identity.dto.response.AuthResponseDto;
import com.onlinepharmacy.identity.dto.request.SignupRequestDto;
import com.onlinepharmacy.identity.exception.ConflictException;
import com.onlinepharmacy.identity.model.User;
import com.onlinepharmacy.identity.repository.UserRepository;
import com.onlinepharmacy.identity.security.JwtTokenService;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthServiceTest {

  @Test
  public void signup_whenEmailExists_throws() {
    UserRepository repo = Mockito.mock(UserRepository.class);
    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
    JwtTokenService jwt = realJwtTokenService();

    Mockito.when(repo.findByEmail("a@b.com")).thenReturn(Optional.of(new User()));

    AuthService svc = new AuthService(repo, encoder, jwt);

    SignupRequestDto req = new SignupRequestDto();
    req.setName("Name");
    req.setEmail("a@b.com");
    req.setMobile("999");
    req.setPassword("pass");

    Assertions.assertThrows(ConflictException.class, () ->
        svc.signup(req)
    );
  }

  @Test
  public void signup_whenNew_createsCustomerRoleAndReturnsToken() {
    UserRepository repo = Mockito.mock(UserRepository.class);
    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
    JwtTokenService jwt = realJwtTokenService();

    Mockito.when(repo.findByEmail("a@b.com")).thenReturn(Optional.empty());
    Mockito.when(encoder.encode("pass")).thenReturn("hash");

    Mockito.when(repo.save(Mockito.any(User.class))).thenAnswer(invocation -> {
      User u = invocation.getArgument(0);
      u.setId(42L);
      return u;
    });

    AuthService svc = new AuthService(repo, encoder, jwt);

    SignupRequestDto req = new SignupRequestDto();
    req.setName("Name");
    req.setEmail("a@b.com");
    req.setMobile("999");
    req.setPassword("pass");

    AuthResponseDto response = svc.signup(req);

    Assertions.assertNotNull(response.getAccessToken());
    Assertions.assertFalse(response.getAccessToken().isBlank());
    Assertions.assertTrue(response.getRoles().contains("CUSTOMER"));
  }

  @Test
  public void login_whenValidCredentials_returnsToken() {
    UserRepository repo = Mockito.mock(UserRepository.class);
    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
    JwtTokenService jwt = realJwtTokenService();

    User user = new User();
    user.setId(100L);
    user.setEmail("a@b.com");
    user.setPasswordHash("hash");
    user.setEnabled(true);

    Mockito.when(repo.findByEmail("a@b.com")).thenReturn(Optional.of(user));
    Mockito.when(encoder.matches("pass", "hash")).thenReturn(true);

    AuthService svc = new AuthService(repo, encoder, jwt);

    com.onlinepharmacy.identity.dto.request.LoginRequestDto req = new com.onlinepharmacy.identity.dto.request.LoginRequestDto();
    req.setEmail("a@b.com");
    req.setPassword("pass");

    AuthResponseDto response = svc.login(req);

    Assertions.assertNotNull(response.getAccessToken());
  }

  @Test
  public void login_whenInvalidPassword_throws() {
    UserRepository repo = Mockito.mock(UserRepository.class);
    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);
    JwtTokenService jwt = realJwtTokenService();

    User user = new User();
    user.setEmail("a@b.com");
    user.setPasswordHash("hash");
    user.setEnabled(true);

    Mockito.when(repo.findByEmail("a@b.com")).thenReturn(Optional.of(user));
    Mockito.when(encoder.matches("wrong", "hash")).thenReturn(false);

    AuthService svc = new AuthService(repo, encoder, jwt);

    com.onlinepharmacy.identity.dto.request.LoginRequestDto req = new com.onlinepharmacy.identity.dto.request.LoginRequestDto();
    req.setEmail("a@b.com");
    req.setPassword("wrong");

    Assertions.assertThrows(com.onlinepharmacy.identity.exception.UnauthorizedException.class, () ->
        svc.login(req)
    );
  }

  @Test
  public void getUserOrThrow_whenValidId_returnsUser() {
    UserRepository repo = Mockito.mock(UserRepository.class);
    AuthService svc = new AuthService(repo, null, null);

    User user = new User();
    user.setId(10L);
    Mockito.when(repo.findById(10L)).thenReturn(Optional.of(user));

    User result = svc.getUserOrThrow("10");
    Assertions.assertEquals(10L, result.getId());
  }

  @Test
  public void getUserOrThrow_whenInvalidFormat_throws() {
    AuthService svc = new AuthService(null, null, null);
    Assertions.assertThrows(com.onlinepharmacy.identity.exception.UnauthorizedException.class, () ->
        svc.getUserOrThrow("abc")
    );
  }

  @Test
  public void getUserOrThrow_whenNotFound_throws() {
    UserRepository repo = Mockito.mock(UserRepository.class);
    Mockito.when(repo.findById(99L)).thenReturn(Optional.empty());
    AuthService svc = new AuthService(repo, null, null);

    Assertions.assertThrows(com.onlinepharmacy.identity.exception.ResourceNotFoundException.class, () ->
        svc.getUserOrThrow("99")
    );
  }

  private JwtTokenService realJwtTokenService() {
    JwtProperties props = new JwtProperties();
    props.setSecret("01234567890123456789012345678901234567890123456789");
    props.setExpirationSeconds(3600);
    return new JwtTokenService(props);
  }
}
