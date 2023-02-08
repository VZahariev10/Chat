package com.example.chatSSE.repository;

import com.example.chatSSE.model.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * From here we get access to the database
 */
@Repository
public class UserRepository implements UserDAO
{

  /**
   * An Object that helps us to run queries
   */
  private final NamedParameterJdbcOperations namedParameterJdbcOperations;

  public UserRepository(NamedParameterJdbcOperations namedParameterJdbcOperations)
  {
    this.namedParameterJdbcOperations = namedParameterJdbcOperations;
  }

  /**
   * Sql query from which we insert users in the database
   */
  @Override
  public User registration(User user)
  {
    String sql = "INSERT INTO users (username, password,isOnline) "
        + "       VALUES (:username, :password,:isOnline)";

    namedParameterJdbcOperations.update(sql,
        new MapSqlParameterSource("username", user.getUserName())
            .addValue("password", user.getPassword())
            .addValue("isOnline", 0));

    return user;
  }

  /**
   * Query to help us find a user by his username and password
   */
  @Override
  public User findByUsernameAndPassword(User user)
  {
    String sql = "SELECT * "
        + "       FROM users"
        + "       WHERE username = :username AND password = :password";

    return namedParameterJdbcOperations.queryForObject(sql,
        new MapSqlParameterSource("username", user.getUserName())
            .addValue("password", user.getPassword()),
        (rs, rowNum) -> {
          User currentUser = new User();
          currentUser.setUserId(rs.getInt("user_id"));
          currentUser.setUserName(rs.getString("userName"));
          currentUser.setPassword(rs.getString("password"));
          currentUser.setIsOnline(rs.getInt("isOnline"));

          return currentUser;
        });
  }

  /**
   * Query to help us find a user by his username
   */
  @Override
  public User findByUsername(String username)
  {
    String sql = "SELECT * "
        + "       FROM users"
        + "       WHERE username = :username";

    return namedParameterJdbcOperations.queryForObject(sql,
        new MapSqlParameterSource("username", username),
        (rs, rowNum) -> {
          User currentUser = new User();
          currentUser.setUserId(rs.getInt("user_id"));
          currentUser.setUserName(rs.getString("userName"));
          currentUser.setPassword(rs.getString("password"));
          currentUser.setIsOnline(rs.getInt("isOnline"));

          return currentUser;
        });
  }

  /**
   * Query to help us find a user by his id
   */
  @Override
  public User findById(Integer id)
  {
    String sql = "SELECT * "
        + "       FROM users "
        + "       WHERE user_id = :user_id";

    return namedParameterJdbcOperations.queryForObject(sql,
        new MapSqlParameterSource("user_id", id),
        (rs, rowNum) -> {
          User currentUser = new User();
          currentUser.setUserId(rs.getInt("user_id"));
          currentUser.setUserName(rs.getString("userName"));
          currentUser.setPassword(rs.getString("password"));
          currentUser.setIsOnline(rs.getInt("isOnline"));

          return currentUser;
        });
  }

  /**
   * Query to help us delete a user by his id
   */
  @Override
  public int deleteById(Integer id)
  {
    String sql = "DELETE "
        + "       FROM users "
        + "       WHERE user_id = :user_id";

    return namedParameterJdbcOperations.update(sql,
        new MapSqlParameterSource("user_id", id));
  }

  /**
   * Query to help us find all users
   */
  @Override
  public List<User> readAllUsers()
  {
    String sql = "SELECT *"
        + "       FROM users "
        + "       WHERE isOnline = 1";

    return namedParameterJdbcOperations.query(sql, new MapSqlParameterSource(),
        (rs, rowNum) -> {
          User currentUser = new User();
          currentUser.setUserId(rs.getInt("user_id"));
          currentUser.setUserName(rs.getString("userName"));
          currentUser.setPassword(rs.getString("password"));
          currentUser.setIsOnline(rs.getInt("isOnline"));

          return currentUser;
        });
  }

  public List<User> readAlllUsers()
  {
    String sql = "SELECT *"
        + "       FROM users ";

    return namedParameterJdbcOperations.query(sql, new MapSqlParameterSource(),
        (rs, rowNum) -> {
          User currentUser = new User();
          currentUser.setUserId(rs.getInt("user_id"));
          currentUser.setUserName(rs.getString("userName"));
          currentUser.setPassword(rs.getString("password"));
          currentUser.setIsOnline(rs.getInt("isOnline"));

          return currentUser;
        });
  }
}