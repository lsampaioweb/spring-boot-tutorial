package com.learning.profiles;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ProfilesApplication implements CommandLineRunner {

  private final Environment environment;

  public ProfilesApplication(Environment environment) {
    this.environment = environment;
  }

  public static void main(String[] args) {
    SpringApplication.run(ProfilesApplication.class, args);
  }

  @Override
  public void run(String... args) {
    String activeProfiles = String.join(", ", environment.getActiveProfiles());
    String environmentName = environment.getProperty("app.environment", "default");

    System.out.println("Active profiles: " + activeProfiles);
    System.out.println("app.environment: " + environmentName);
  }

}
