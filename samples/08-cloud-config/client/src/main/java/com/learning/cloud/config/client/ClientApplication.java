package com.learning.cloud.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

  @Value("${user.role}")
  private String role;

  @Value("${server.port}")
  private int serverPort;

  @Bean
  public CommandLineRunner getPropertyFromCloudConfigServer() {
    return (args) -> {
      System.out.println(String.format("Type: %s - %d", role, serverPort));
    };
  }

}
