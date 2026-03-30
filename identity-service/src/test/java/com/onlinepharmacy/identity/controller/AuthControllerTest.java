package com.onlinepharmacy.identity.controller;

import com.onlinepharmacy.identity.dto.request.LoginRequestDto;
import com.onlinepharmacy.identity.dto.request.SignupRequestDto;
import com.onlinepharmacy.identity.dto.response.AuthResponseDto;
import com.onlinepharmacy.identity.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void signup_returns200() throws Exception {
    SignupRequestDto req = new SignupRequestDto();
    req.setEmail("test@test.com");
    req.setName("Test");
    req.setPassword("password");
    req.setMobile("1234567890");

    Mockito.when(authService.signup(Mockito.any())).thenReturn(new AuthResponseDto());

    mockMvc.perform(post("/api/auth/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk());
  }

  @Test
  public void login_returns200() throws Exception {
    LoginRequestDto req = new LoginRequestDto();
    req.setEmail("test@test.com");
    req.setPassword("password");

    Mockito.when(authService.login(Mockito.any())).thenReturn(new AuthResponseDto());

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk());
  }
}
