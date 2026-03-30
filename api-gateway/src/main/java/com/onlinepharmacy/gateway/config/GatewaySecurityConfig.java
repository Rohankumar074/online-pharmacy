package com.onlinepharmacy.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
public class GatewaySecurityConfig {

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .cors(cors -> cors.configurationSource(exchange -> {
          String allowedOrigin = System.getenv().getOrDefault("CORS_ALLOWED_ORIGIN", "http://localhost:5173");
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOriginPatterns(List.of(allowedOrigin, "http://localhost:*", "http://127.0.0.1:*", "*"));
          config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
          config.setAllowedHeaders(List.of("*"));
          config.setAllowCredentials(false);
          return config;
        }))
        .authorizeExchange(exchanges -> exchanges
            // Gateway relies on JwtValidationWebFilter for auth.
            .anyExchange().permitAll()
        )
        .build();
  }
}

