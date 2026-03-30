package com.onlinepharmacy.catalog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String userId = request.getHeader("X-Auth-UserId");
    String rolesHeader = request.getHeader("X-Auth-Roles");

    if (userId != null && !userId.isBlank()) {
      List<SimpleGrantedAuthority> authorities = new ArrayList<>();
      if (rolesHeader != null && !rolesHeader.isBlank()) {
        for (String role : rolesHeader.split(",")) {
          String normalized = role.startsWith("ROLE_") ? role : "ROLE_" + role;
          authorities.add(new SimpleGrantedAuthority(normalized));
        }
      }
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }
}
