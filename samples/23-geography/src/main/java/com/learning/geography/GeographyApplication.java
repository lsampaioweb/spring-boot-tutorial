package com.learning.geography;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GeographyApplication {

  public static void main(String[] args) {
    SpringApplication.run(GeographyApplication.class, args);
  }

}
