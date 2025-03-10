# Spring Boot + PostgreSQL (JDBCClient) Tutorial

This tutorial demonstrates how to integrate **Spring Boot** with **PostgreSQL** using `JdbcClient`, without JPA. The project follows best practices, including **structured logging** and **i18n messages**.

## 1. Prerequisites
Before running this project, ensure you have:
- **Java 21** installed.
- **Maven** installed.
- **Docker** installed (for PostgreSQL).
- **A PostgreSQL instance running in Docker**.

## 2. Project Setup
### 2.1 Dependencies

This project uses:
```xml
<dependencies>
  <!-- Spring Boot JDBC -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
  </dependency>

  <!-- PostgreSQL Driver -->
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
  </dependency>

  <!-- Lombok -->
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
  </dependency>
</dependencies>
```

### 2.2 Database Configuration (`application.yml`)
```yaml
spring:
  datasource:
    driver-class-name: "org.postgresql.Driver"
    url: "jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:mydatabase}"
    username: "${DB_USER}"
    password: "${DB_PASSWORD}"
    hikari:
      # Maximum number of connections in the pool (default: 10).
      maximum-pool-size: 10
      # Minimum number of idle connections in the pool (default: same as max).
      minimum-idle: 2
      # Time (ms) before an idle connection is removed (must be less than max-lifetime).
      idle-timeout: 30000
      # Maximum time (ms) to wait for a connection before throwing an error.
      connection-timeout: 20000
```

### 2.3 Environment Variables

To configure the database connection, you can set the following environment variables:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=mydatabase
export DB_USER=myuser
export DB_PASSWORD=mypassword
```

## 3. Code Structure
### 3.1 Model
```java
package com.learning.postgres.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Model {
  private Long id;
  private String name;
  private String email;
}
```

### 3.2 Repository
```java
package com.learning.postgres.user;

import com.learning.postgres.exception.DatabaseException;
import com.learning.postgres.util.MessageSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
public class Repository {

  private final JdbcClient jdbcClient;

  public Repository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Transactional
  public void saveAll(List<Model> users) {
    try {
      log.info(MessageSourceHolder.getMessage("log.user.inserting.batch", users.size()));

      String sql = "INSERT INTO users (name, email) VALUES (:name, :email)";

      for (Model user : users) {
        jdbcClient.sql(sql)
            .param("name", user.getName())
            .param("email", user.getEmail())
            .update();
      }
    } catch (Exception e) {
      log.error(MessageSourceHolder.getMessage("error.user.insert.batch"), e);
      throw new DatabaseException("error.user.insert.batch", e);
    }
  }
}
```

## 4. Running the Project
### 4.1 Start the Application
```bash
mvn spring-boot:run
```

### 4.2 Test API Endpoints
#### Create a User

```bash
curl -X POST -H "Content-Type: application/json" -d '{"name": "Alice", "email": "alice@example.com"}' http://localhost:8080/users
```

#### Fetch All Users:

```bash
curl http://localhost:8080/users
```

#### Batch Insert Users:

```bash
curl -X POST -H "Content-Type: application/json" -d '[
  {"name": "Alice", "email": "alice@example.com"},
  {"name": "Bob", "email": "bob@example.com"}
]' http://localhost:8080/users/batch
```

#### Fetch a User by ID:

```bash
curl http://localhost:8080/users/1
```

#### Delete a User:

```bash
curl -X DELETE http://localhost:8080/users/1
```

[Go Back](../../../README.md)

#
### Created by:

1. Luciano Sampaio.
