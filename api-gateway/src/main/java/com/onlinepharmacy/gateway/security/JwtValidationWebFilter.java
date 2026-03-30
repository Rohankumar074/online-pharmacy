package com.onlinepharmacy.gateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepharmacy.gateway.dto.ApiError;
import com.onlinepharmacy.gateway.dto.response.ValidatedTokenResponseDto;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Validates JWT at the gateway.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtValidationWebFilter implements WebFilter {

  private final ObjectMapper objectMapper;
  private final WebClient webClient;

  public JwtValidationWebFilter(
      ObjectMapper objectMapper,
      WebClient.Builder webClientBuilder
  ) {
    this.objectMapper = objectMapper;
    // @LoadBalanced WebClient natively requires the http:// protocol internally (not lb://)
    this.webClient = webClientBuilder.baseUrl("http://identity-service").build();
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();
    if (shouldSkip(path)) {
      return chain.filter(exchange);
    }

    String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      return writeError(exchange, UNAUTHORIZED, "Missing Bearer token");
    }

    return webClient.get()
        .uri("/api/auth/validate")
        .header(HttpHeaders.AUTHORIZATION, authorization)
        .retrieve()
        .bodyToMono(ValidatedTokenResponseDto.class)
        .flatMap(validated -> {
          if (validated == null) {
            return writeError(exchange, UNAUTHORIZED, "Invalid token");
          }
          String userId = validated.getUserId();
          if (userId == null || userId.isBlank()) {
            return writeError(exchange, UNAUTHORIZED, "Invalid token: missing userId");
          }

          List<String> roles = validated.getRoles();
          if (roles == null) {
            roles = Collections.emptyList();
          }
          final List<String> finalRoles = roles;

          if (path.startsWith("/api/admin/") && finalRoles.stream().noneMatch(r -> "ROLE_ADMIN".equals(r) || "ADMIN".equals(r))) {
            return writeError(exchange, HttpStatus.FORBIDDEN, "Admin role required");
          }

          ServerWebExchange enrichedExchange = exchange.mutate()
              .request(builder -> builder
                  .header("X-Auth-UserId", userId)
                  .header("X-Auth-Roles", String.join(",", finalRoles)))
              .build();

          return chain.filter(enrichedExchange);
        })
        .onErrorResume(ex -> writeError(exchange, UNAUTHORIZED, "Invalid token: " + ex.getMessage()));
  }

  private boolean shouldSkip(String path) {
    return path.startsWith("/api/auth/signup")
        || path.startsWith("/api/auth/login")
        || path.startsWith("/v3/api-docs")
        || path.startsWith("/swagger-ui")
        || path.startsWith("/swagger-ui.html")
        || path.startsWith("/webjars/");
  }

  private Mono<Void> writeError(ServerWebExchange exchange, HttpStatus status, String message) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(status);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    ApiError apiError = new ApiError(
        status.value(),
        status.getReasonPhrase(),
        message,
        exchange.getRequest().getURI().getPath()
    );

    byte[] bytes;
    try {
      bytes = objectMapper.writeValueAsBytes(apiError);
    } catch (Exception e) {
      bytes = ("{\"message\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
    }

    DataBufferFactory bufferFactory = response.bufferFactory();
    DataBuffer buffer = bufferFactory.wrap(bytes);
    return response.writeWith(Mono.just(buffer));
  }
}

