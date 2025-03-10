package com.learning.postgres.user;

import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.learning.postgres.db.DatabaseException;
import com.learning.postgres.i18n.MessageSourceHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserRepository {

  private final JdbcClient jdbcClient;

  public UserRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public List<Model> findAll() {
    log.info(MessageSourceHolder.getMessage("log.user.fetching.all"));

    String sql = "SELECT id, name, email FROM users";

    return jdbcClient
        .sql(sql)
        .query(Model.class)
        .list();
  }

  public Model findById(Long id) {
    log.info(MessageSourceHolder.getMessage("log.user.fetching.id", id));

    String sql = "SELECT id, name, email FROM users WHERE id = :id";

    return jdbcClient
        .sql(sql)
        .param("id", id)
        .query(Model.class)
        .optional()
        .orElseThrow(() -> {
          log.warn(MessageSourceHolder.getMessage("error.user.not.found", id));

          return new UserNotFoundException(id);
        });
  }

  public void save(Model model) {
    try {

      String sql = "INSERT INTO users (name, email) VALUES (:name, :email)";

      jdbcClient
          .sql(sql)
          .param("name", model.getName())
          .param("email", model.getEmail())
          .update();
    } catch (Exception e) {
      throw new DatabaseException("Error inserting user", e);
    }
  }

  @Transactional
  public void saveAll(List<Model> list) {
    try {
      log.info(MessageSourceHolder.getMessage("log.user.inserting.batch", list.size()));

      String sql = "INSERT INTO users (name, email) VALUES (:name, :email)";
      for (Model item : list) {
        jdbcClient.sql(sql)
            .param("name", item.getName())
            .param("email", item.getEmail())
            .update();
      }
    } catch (Exception e) {
      log.error(MessageSourceHolder.getMessage("error.user.insert.batch"), e);

      throw new DatabaseException("error.user.insert.batch", e);
    }
  }

  public void deleteById(Long id) {
    String sql = "DELETE FROM users WHERE id = :id";

    int rowsAffected = jdbcClient
        .sql(sql)
        .param("id", id)
        .update();

    if (rowsAffected == 0) {
      throw new UserNotFoundException(id);
    }
  }
}
