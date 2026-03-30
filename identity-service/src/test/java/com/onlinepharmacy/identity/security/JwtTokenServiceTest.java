package com.onlinepharmacy.identity.security;

import com.onlinepharmacy.identity.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwtTokenServiceTest {

  @Test
  public void generateToken_includesUserIdAndRoles() {
    JwtProperties props = new JwtProperties();
    props.setSecret("01234567890123456789012345678901234567890123456789"); // >= 32 bytes
    props.setExpirationSeconds(3600);

    JwtTokenService svc = new JwtTokenService(props);
    String token = svc.generateToken("123", List.of("CUSTOMER", "ADMIN"));

    SecretKey key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    Claims claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    Assertions.assertEquals("123", claims.get("userId", String.class));
    @SuppressWarnings("unchecked")
    List<String> roles = (List<String>) claims.get("roles", List.class);
    Assertions.assertTrue(roles.contains("CUSTOMER"));
    Assertions.assertTrue(roles.contains("ADMIN"));
  }
}

