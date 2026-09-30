package com.learning.postgres.user;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.learning.postgres.db.DatabaseException;

@Repository
class UserRepositoryImpl implements UserRepository {

  private static final String ERROR_USER_INSERT = "error.user.insert";
  private static final String ERROR_USER_UPDATE = "error.user.update";
  private static final String ERROR_USER_DELETE = "error.user.delete";
  private static final String ERROR_SORT_PROPERTY_INVALID = "error.sort.property.invalid";

  private final JdbcClient jdbcClient;
  private final UserSqlConfigurationProperties sqlProperties;

  UserRepositoryImpl(JdbcClient jdbcClient, UserSqlConfigurationProperties sqlProperties) {
    this.jdbcClient = jdbcClient;
    this.sqlProperties = sqlProperties;
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    long total = jdbcClient.sql(sqlProperties.countAll()).query(Long.class).single();
    String sql = sqlProperties.findAll() + " ORDER BY " + orderBy(pageable.getSort())
        + " LIMIT :limit OFFSET :offset";
    List<User> content = jdbcClient.sql(sql)
        .param("limit", pageable.getPageSize())
        .param("offset", pageable.getOffset())
        .query(User.class)
        .list();

    return new PageImpl<>(content, pageable, total);
  }

  @Override
  public Optional<User> findById(Long id) {
    return jdbcClient.sql(sqlProperties.findById())
        .param(UserSqlColumns.ID, id).query(User.class).optional();
  }

  @Override
  public User insert(User user) {
    try {
      return jdbcClient.sql(sqlProperties.insert())
          .param(UserSqlColumns.NAME, user.name())
          .param(UserSqlColumns.EMAIL, user.email())
          .query(User.class)
          .single();
    } catch (Exception e) {
      throw new DatabaseException(ERROR_USER_INSERT, e);
    }
  }

  @Override
  public int update(User user) {
    try {
      int rowsAffected = jdbcClient.sql(sqlProperties.update())
          .param(UserSqlColumns.ID, user.id()).param(UserSqlColumns.NAME, user.name())
          .param(UserSqlColumns.EMAIL, user.email()).update();
      if (rowsAffected == 0) {
        throw new UserNotFoundException(user.id());
      }
      return rowsAffected;
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new DatabaseException(ERROR_USER_UPDATE, e);
    }
  }

  @Override
  public int deleteById(Long id) {
    try {
      int rowsAffected = jdbcClient.sql(sqlProperties.deleteById())
          .param(UserSqlColumns.ID, id).update();
      if (rowsAffected == 0) {
        throw new UserNotFoundException(id);
      }
      return rowsAffected;
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new DatabaseException(ERROR_USER_DELETE, e);
    }
  }

  private String orderBy(Sort sort) {
    if (sort == null || sort.isUnsorted()) {
      return UserSqlColumns.ID + " ASC";
    }

    return sort.stream()
        .map(order -> columnFor(order.getProperty()) + " " + order.getDirection().name())
        .collect(Collectors.joining(", "));
  }

  private String columnFor(String property) {
    return switch (property.toLowerCase(Locale.ROOT)) {
      case "id" -> UserSqlColumns.ID;
      case "name" -> UserSqlColumns.NAME;
      case "email" -> UserSqlColumns.EMAIL;
      default -> throw new IllegalArgumentException(ERROR_SORT_PROPERTY_INVALID);
    };
  }
}
