package com.example.chatSSE.service;

import com.example.chatSSE.dto.UserDTO;
import com.example.chatSSE.model.User;
import com.example.chatSSE.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * A Class that helps us validate and manipulate the data returned by the repositories
 */
@Service
public class UserService
{

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository)
  {
    this.userRepository = userRepository;
  }

  /**
   * We validate the data and then we return the UserDTO
   */
  public String registration(User user)
  {
    UserDTO dto = new UserDTO();
    dto.setUserName(user.getUserName());

    if (!findByUsernameAndPassword(user)) {
      userRepository.registration(user);

      return dto.getUserName() + " has registered completely";
    }
    return "Username already exists!";
  }

  /**
   * Here we return a user when we set his data from the database
   */
  public User findById(Integer id)
  {

    try {
      User user = userRepository.findById(id);
      if (user != null) {
        user.setUserId(user.getUserId());
        user.setUserName(user.getUserName());
        user.setIsOnline(user.getIsOnline());

        return user;
      }
    }
    catch (EmptyResultDataAccessException ex) {
      ex.getMessage();
    }
    return null;
  }

  /**
   * Here we return a boolean(true,false) depending on whether we found a user or not
   */
  public boolean findByUsernameAndPassword(User user)
  {
    try {
      User existingUser = userRepository.findByUsernameAndPassword(user);

      if (existingUser
          .getUserName()
          .equalsIgnoreCase(user.getUserName()) &&
          existingUser
              .getPassword()
              .equalsIgnoreCase(user.getPassword())) {
        return true;
      }

    }
    catch (EmptyResultDataAccessException e) {
      e.getMessage();
    }
    catch (NullPointerException e) {
      e.getMessage();
    }
    return false;
  }

  /**
   * If the database contains the given username then we return true
   */
  public boolean findByUsername(String username)
  {
    try {
      User user1 = userRepository.findByUsername(username);

      if (user1
          .getUserName()
          .equalsIgnoreCase(username)) {
        return true;
      }
    }
    catch (EmptyResultDataAccessException exception) {
      exception.getMessage();
    }
    return false;
  }

  /**
   * Here we return a list of users
   */
  public List<UserDTO> readAllUsers()
  {
    List<User> users = userRepository.readAllUsers();
    List<UserDTO> dtoList = new ArrayList<>();

    for (int i = 0; i < users.size(); i++) {
      dtoList.add(new UserDTO());

      dtoList
          .get(i)
          .setUserName(users
              .get(i)
              .getUserName());
    }
    return dtoList;
  }

  /**
   * Here we return information whether the user has been deleted or not
   */
  public String deleteById(Integer id)
  {
    try {
      int rows = userRepository.deleteById(id);
      if (rows != 0) {
        return "User deleted successfully";
      }
    }
    catch (EmptyResultDataAccessException e) {
      e.getMessage();
    }
    return "User not found!";
  }

  public UserRepository getUserRepository()
  {
    return userRepository;
  }
}