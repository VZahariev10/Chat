package com.example.chatSSE.controller;

import com.example.chatSSE.dto.UserDTO;
import com.example.chatSSE.model.User;
import com.example.chatSSE.service.UserService;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * A Class that helps us request and execute HTTP Methods
 */
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * A Http POST method that provides us with the option to register
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("registration")
    public String registration(@Valid @RequestBody User user) {
        return userService.registration(user);
    }

    /**
     * A Http GET method that provides us with the option of getting all users
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("getAllUsers")
    public List<UserDTO> getAllUsers() {
        return userService.readAllUsers();
    }

    /**
     * A Http method that provides us with the option to delete user by id
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("deleteById/{id}")
    public String deleteById(@Valid @PathVariable("id") Integer id) {
        return userService.deleteById(id);
    }
}