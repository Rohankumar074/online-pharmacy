package com.onlinepharmacy.identity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
  /**
   * HMAC secret used to sign and verify JWT tokens.
   * Provide via env var `JWT_SECRET`.
   */
  private String secret;

  /**
   * Token validity in seconds.
   */
  private long expirationSeconds = 86400;

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public long getExpirationSeconds() {
    return expirationSeconds;
  }

  public void setExpirationSeconds(long expirationSeconds) {
    this.expirationSeconds = expirationSeconds;
  }
}

