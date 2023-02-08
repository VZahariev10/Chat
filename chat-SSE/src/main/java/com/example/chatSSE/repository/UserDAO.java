package com.example.chatSSE.repository;

import com.example.chatSSE.model.User;

import java.util.List;

public interface UserDAO
{

  User registration(User user);

  User findById(Integer id);

  int deleteById(Integer id);

  List<User> readAllUsers();

  User findByUsernameAndPassword(User user);

  User findByUsername(String username);
}