package com.learning.postgres.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.learning.postgres.db.DatabaseException;
import com.learning.postgres.i18n.MessageSourceHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
class UserRepository {

  private final JdbcClient jdbcClient;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final MessageSourceHolder messageSourceHolder;
  private final String findAllSql;
  private final String findByIdSql;
  private final String insertSql;
  private final String deleteByIdSql;

  UserRepository(
      JdbcClient jdbcClient,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      MessageSourceHolder messageSourceHolder,
      @Value("${sql.users.find-all}") String findAllSql,
      @Value("${sql.users.find-by-id}") String findByIdSql,
      @Value("${sql.users.insert}") String insertSql,
      @Value("${sql.users.delete-by-id}") String deleteByIdSql) {
    this.jdbcClient = jdbcClient;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.messageSourceHolder = messageSourceHolder;
    this.findAllSql = findAllSql;
    this.findByIdSql = findByIdSql;
    this.insertSql = insertSql;
    this.deleteByIdSql = deleteByIdSql;
  }

  List<Model> findAll() {
    log.info(messageSourceHolder.getMessage("log.user.fetching.all"));

    return jdbcClient
        .sql(findAllSql)
        .query(Model.class)
        .list();
  }

  Model findById(Long id) {
    log.info(messageSourceHolder.getMessage("log.user.fetching.id", id));

    return jdbcClient
        .sql(findByIdSql)
        .param("id", id)
        .query(Model.class)
        .optional()
        .orElseThrow(() -> {
          log.warn(messageSourceHolder.getMessage("error.user.not.found", id));

          return new UserNotFoundException(id);
        });
  }

  void save(Model model) {
    log.info(messageSourceHolder.getMessage("log.user.inserting"));

    try {
      jdbcClient
          .sql(insertSql)
          .param("name", model.getName())
          .param("email", model.getEmail())
          .update();
    } catch (Exception e) {
      throw new DatabaseException("error.user.insert", e);
    }
  }

  @Transactional
  void saveAll(List<Model> list) {
    log.info(messageSourceHolder.getMessage("log.user.inserting.batch", list.size()));

    try {
      namedParameterJdbcTemplate.batchUpdate(insertSql, SqlParameterSourceUtils.createBatch(list));
    } catch (Exception e) {
      log.error(messageSourceHolder.getMessage("error.user.insert.batch"), e);

      throw new DatabaseException("error.user.insert.batch", e);
    }
  }

  void deleteById(Long id) {
    log.info(messageSourceHolder.getMessage("log.user.deleting.id", id));

    int rowsAffected = jdbcClient
        .sql(deleteByIdSql)
        .param("id", id)
        .update();

    if (rowsAffected == 0) {
      throw new UserNotFoundException(id);
    }
  }
}
