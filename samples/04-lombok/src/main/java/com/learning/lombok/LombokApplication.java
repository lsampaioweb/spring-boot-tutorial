package com.learning.lombok;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import com.learning.lombok.entity.UserBuilder;
import com.learning.lombok.entity.UserData;
import com.learning.lombok.entity.UserEqualsAndHashCode;
import com.learning.lombok.entity.UserGetSet;
import com.learning.lombok.entity.UserToString;
import com.learning.lombok.entity.UserWithConstructors;
import com.learning.lombok.service.UserService;

@SpringBootApplication
public class LombokApplication {

  public static void main(String[] args) {
    SpringApplication.run(LombokApplication.class, args);
  }

  @Bean
  @Order(1)
  public CommandLineRunner demoGetSet() {
    return (args) -> {
      System.out.println("Demo GetSet");

      UserGetSet user = new UserGetSet();
      user.setName("Luciano Sampaio");
      user.setAge(41);

      System.out.println(user.getName());
      System.out.println(user.getAge());
    };
  }

  @Bean
  @Order(2)
  public CommandLineRunner demoToString() {
    return (args) -> {
      System.out.println("\nDemo ToString");

      UserToString user = new UserToString();
      user.setName("Luciano Sampaio");
      user.setAge(41);

      System.out.println(user);
    };
  }

  @Bean
  @Order(3)
  public CommandLineRunner demoEqualsAndHashCode() {
    return (args) -> {
      System.out.println("\nDemo EqualsAndHashCode");

      UserEqualsAndHashCode user = new UserEqualsAndHashCode();
      user.setName("Luciano Sampaio");
      user.setAge(41);

      UserEqualsAndHashCode sameUser = new UserEqualsAndHashCode();
      sameUser.setName("Luciano Sampaio");
      sameUser.setAge(41);

      UserEqualsAndHashCode differentUser = new UserEqualsAndHashCode();
      differentUser.setName("Other Name");
      differentUser.setAge(10);

      // Testing equals() method.
      System.out.println("user equals sameUser: " + user.equals(sameUser)); // Expected: true.
      System.out.println("user equals differentUser: " + user.equals(differentUser)); // Expected: false.

      // Testing hashCode() method.
      System.out.println("user.hashCode(): " + user.hashCode());
      System.out.println("sameUser.hashCode(): " + sameUser.hashCode());
      System.out.println("differentUser.hashCode(): " + differentUser.hashCode());

      // Checking if hashCode of equal objects is the same.
      System.out.println("HashCode equality: " + (user.hashCode() == sameUser.hashCode())); // Expected: true.
      System.out.println("HashCode equality: " + (user.hashCode() == differentUser.hashCode())); // Expected: false.
    };
  }

  @Bean
  @Order(4)
  public CommandLineRunner demoWithConstructors() {
    return (args) -> {
      System.out.println("\nDemo WithConstructors");

      UserWithConstructors user = new UserWithConstructors();
      System.out.println(user);

      UserWithConstructors user2 = new UserWithConstructors("Just Name");
      System.out.println(user2);

      UserWithConstructors user3 = new UserWithConstructors("Name", 41);
      System.out.println(user3);
    };
  }

  @Bean
  @Order(5)
  public CommandLineRunner demoData() {
    return (args) -> {
      System.out.println("\nDemo Data");

      UserData user = new UserData("Luciano");
      user.setAge(41);

      System.out.println(user.getName());
      System.out.println(user.getAge());
      System.out.println(user);

    };
  }

  @Bean
  @Order(6)
  public CommandLineRunner demoBuilder() {
    return (args) -> {
      System.out.println("\nDemo Builder");

      UserBuilder user = UserBuilder.builder()
        .name("Luciano")
        .age(41)
        .build();

      System.out.println(user);
    };
  }

  @Bean
  @Order(7)
  public CommandLineRunner demoUserService(UserService userService) {
    return (args) -> {
      System.out.println("\nDemo UserService");

      userService.logMessage();
    };
  }

}
