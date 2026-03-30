package com.onlinepharmacy.catalog.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attributes != null) {
        HttpServletRequest request = attributes.getRequest();
        
        String userId = request.getHeader("X-Auth-UserId");
        String roles = request.getHeader("X-Auth-Roles");
        
        if (userId != null && !userId.isBlank()) {
          requestTemplate.header("X-Auth-UserId", userId);
        }
        if (roles != null && !roles.isBlank()) {
          requestTemplate.header("X-Auth-Roles", roles);
        }
      }
    };
  }
}
