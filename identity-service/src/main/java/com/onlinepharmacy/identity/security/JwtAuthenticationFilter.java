package com.onlinepharmacy.identity.security;

import com.onlinepharmacy.identity.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProperties jwtProperties;

  public JwtAuthenticationFilter(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    String requestPath = request.getRequestURI();
    
    // Skip JWT validation for public endpoints
    if (isPublicEndpoint(requestPath)) {
      filterChain.doFilter(request, response);
      return;
    }
    
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring("Bearer ".length());
    try {
      SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
      Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

      String userId = claims.get("userId", String.class);
      @SuppressWarnings("unchecked") // JWT claims API only supports raw List.class; roles are always List<String> per JwtTokenService
      List<String> roles = (List<String>) claims.get("roles", List.class);
      if (userId == null || userId.isBlank()) {
        filterChain.doFilter(request, response);
        return;
      }

      List<SimpleGrantedAuthority> authorities = new ArrayList<>();
      if (roles != null) {
        for (String role : roles) {
          String normalized = role.startsWith("ROLE_") ? role : "ROLE_" + role;
          authorities.add(new SimpleGrantedAuthority(normalized));
        }
      }

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userId, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception ignored) {
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }

  private boolean isPublicEndpoint(String path) {
    return path.startsWith("/api/auth/signup") || 
           path.startsWith("/api/auth/login") ||
           path.startsWith("/api/users/") ||
           path.startsWith("/v3/api-docs") ||
           path.startsWith("/swagger-ui");
  }
}