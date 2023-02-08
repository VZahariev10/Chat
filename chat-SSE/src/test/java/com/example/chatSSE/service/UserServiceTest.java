package com.example.chatSSE.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import com.example.chatSSE.model.User;
import com.example.chatSSE.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SpringBootTest
public class UserServiceTest
{

  @Mock
  private UserRepository userRepository;
  private UserService    userService;

  @BeforeMethod
  public void setUp()
  {
    userRepository = Mockito.mock(UserRepository.class);
    userService = new UserService(userRepository);
  }

  @Test
  public void given_valid_user_When_user_registers_Then_register_user()
  {
    User newUser = new User(3, "test", "test", 0);
    User expectedUser = new User(3, "test", "test", 0);
    when(userRepository.registration(newUser)).thenReturn(expectedUser);
    String actualUser = userService.registration(newUser);
    assertEquals(actualUser, expectedUser.getUserName() + " has registered completely");
    verify(userRepository).registration(newUser);
  }

  @Test
  public void given_existing_username_When_user_registers_Then_tell_them_to_pick_another_one()
  {
    User newUser = new User(3, "test", "test", 0);
    User expectedUser = new User(3, "test", "test", 0);
    when(userRepository.findByUsernameAndPassword(newUser)).thenReturn(expectedUser);
    String actualUser = userService.registration(newUser);
    assertEquals(actualUser, "Username already exists!");
  }

  @Test
  public void given_valid_id_When_finding_by_id_Then_return_user()
  {
    Integer id = 3;
    User user = new User(id, "test", "test,", 0);
    when(userRepository.findById(Mockito.anyInt())).thenReturn(user);
    User actualPublisher = userService.findById(id);
    assertEquals(actualPublisher, user);
    verify(userRepository, times(1)).findById(id);
  }

  @Test
  public void given_invalid_id_When_finding_by_id_Then_return_null()
  {
    Integer id = 3;
    when(userRepository.findById(id)).thenReturn(null);
    User actualPublisher = userService.findById(id);
    assertNull(actualPublisher);
    verify(userRepository, times(1)).findById(id);
  }

  @Test
  public void given_valid_username_When_finding_by_username_Then_return_true()
  {
    User newUser = new User(3, "Ivana", "test", 0);
    User expectedUser = new User(3, "Ivana", "test", 0);
    when(userRepository.findByUsername(newUser.getUserName())).thenReturn(expectedUser);
    boolean actualUser = userService.findByUsername(newUser.getUserName());
    assertTrue(actualUser);
    verify(userRepository).findByUsername(newUser.getUserName());
  }

  @Test
  public void given_invalid_username_When_finding_by_username_Then_return_false()
  {
    User newUser = new User(3, "Ivana", "test", 0);
    User expectedUser = new User(3, "Ivan", "test", 0);
    when(userRepository.findByUsername(newUser.getUserName())).thenReturn(expectedUser);
    boolean actualUser = userService.findByUsername(newUser.getUserName());
    assertFalse(actualUser);
    verify(userRepository).findByUsername(newUser.getUserName());
  }

  @Test
  public void given_valid_username_and_password_When_finding_by_username_and_password_Then_return_true()
  {
    User newUser = new User(3, "Ivana", "test", 0);
    User expectedUser = new User(3, "Ivana", "test", 0);
    when(userRepository.findByUsernameAndPassword(newUser)).thenReturn(expectedUser);
    boolean actualUser = userService.findByUsernameAndPassword(newUser);
    assertTrue(actualUser);
    verify(userRepository).findByUsernameAndPassword(newUser);
  }

  @Test
  public void given_invalid_username_and_password_When_finding_by_username_and_password_Then_return_false()
  {
    User newUser = new User(3, "Ivana", "test", 0);
    User expectedUser = new User(3, "Ivan", "testt", 0);
    when(userRepository.findByUsernameAndPassword(newUser)).thenReturn(expectedUser);
    boolean actualUser = userService.findByUsernameAndPassword(newUser);
    assertFalse(actualUser);
    verify(userRepository).findByUsernameAndPassword(newUser);
  }

  @Test
  @DisplayName("given valid id")
  public void given_valid_id_When_delete_by_id_Then_deleted_successfully(){
    Integer id = 4;
    Integer rows = 1;
    when(userRepository.deleteById(id)).thenReturn(rows);
    String affectedRows = userService.deleteById(id);
    verify(userRepository).deleteById(id);
    assertEquals(affectedRows,"User deleted successfully");

  }
}