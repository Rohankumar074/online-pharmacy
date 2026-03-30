package com.onlinepharmacy.gateway;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ApiGatewayApplication {

  public static void main(String[] args) {
		java.io.File dir = new java.io.File(".").getAbsoluteFile();
		while (dir != null && !new java.io.File(dir, ".env").exists()) {
			dir = dir.getParentFile();
		}

		if (dir != null) {
			System.out.println("[BOOTSTRAP] Found .env at: " + dir.getAbsolutePath());
			Dotenv.configure().directory(dir.getAbsolutePath()).load().entries().forEach(entry -> {
				System.setProperty(entry.getKey(), entry.getValue());
			});
			System.out.println("[BOOTSTRAP] .env loaded into System properties successfully.");
		} else {
			System.out.println("[BOOTSTRAP] WARNING: .env file not found anywhere in the parent hierarchy.");
		}

    SpringApplication.run(ApiGatewayApplication.class, args);
  }

  @Bean
  @LoadBalanced
  public WebClient.Builder loadBalancedWebClientBuilder() {
    return WebClient.builder();
  }
}

