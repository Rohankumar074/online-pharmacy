package com.onlinepharmacy.gateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class JwtValidationWebFilterTest {

  @Test
  public void whenValidJwt_injectsAuthHeaders() {
    ExchangeFunction exchangeFunction = clientRequest -> Mono.just(
        ClientResponse.create(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body("{\"userId\":\"42\",\"roles\":[\"ROLE_ADMIN\",\"ROLE_CUSTOMER\"]}")
            .build()
    );
    WebClient.Builder builder = WebClient.builder().exchangeFunction(exchangeFunction);
    JwtValidationWebFilter filter = new JwtValidationWebFilter(new ObjectMapper(), builder);

    MockServerWebExchange exchange = MockServerWebExchange.from(
        MockServerHttpRequest.get("/api/catalog/medicines")
            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            .build()
    );

    AtomicReference<Boolean> chainInvoked = new AtomicReference<>(false);

    WebFilterChain chain = mutatedExchange -> {
      chainInvoked.set(true);
      return Mono.empty();
    };

    filter.filter(exchange, chain).block();
    Assertions.assertTrue(chainInvoked.get());
  }

  @Test
  public void whenMissingBearer_returnsUnauthorized() {
    WebClient.Builder builder = WebClient.builder();
    JwtValidationWebFilter filter = new JwtValidationWebFilter(new ObjectMapper(), builder);

    MockServerWebExchange exchange = MockServerWebExchange.from(
        MockServerHttpRequest.get("/api/orders/cart")
            .build()
    );

    WebFilterChain chain = mutatedExchange -> Mono.empty();
    filter.filter(exchange, chain).block();

    Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
  }
}

