package com.onlinepharmacy.identity.security;

import com.onlinepharmacy.identity.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

  private final JwtProperties jwtProperties;

  public JwtTokenService(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
  }

  public String generateToken(String userId, List<String> roles) {
    SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    Instant now = Instant.now();
    Date exp = Date.from(now.plusSeconds(jwtProperties.getExpirationSeconds()));

    return Jwts.builder()
        .issuedAt(Date.from(now))
        .expiration(exp)
        .claim("userId", userId)
        .claim("roles", roles)
        .signWith(key, Jwts.SIG.HS256)
        .compact();
  }
}

