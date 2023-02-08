package com.example.chatSSE.service;

import com.example.chatSSE.model.Message;
import com.example.chatSSE.model.User;
import com.example.chatSSE.repository.MessageRepository;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A Class that helps us validate and manipulate the data returned by the repositories
 */
@Service
public class MessageService
{
  private final MessageRepository messageRepository;
  private final UserService       userService;
  private final EmitterService    emitterService;

  public MessageService(MessageRepository messageRepository,
      UserService userService,
      EmitterService emitterService)
  {
    this.messageRepository = messageRepository;
    this.userService = userService;
    this.emitterService = emitterService;
  }

  /**
   * When the given user logouts, then we unsubscribe them from their emitter
   */
  public String logOut(UserDetails userDetails)
  {
    if (userService.findByUsername(userDetails.getUsername())) {

      messageRepository.logOut(userDetails.getUsername());
      emitterService
          .getSseSessions()
          .get(userDetails.getUsername())
          .complete();
      return userDetails.getUsername() + " has logged out successfully";
    }
    return "Invalid username!";
  }

  /**
   * When the given user logins, we subscribe him to an emitter,
   * and we return information whether the user has logged in or not
   */
  public SseEmitter subscribe(String username)
  {
    final SseEmitter emitter = new SseEmitter(300000L);
    emitterService
        .getSseSessions()
        .put(username, emitter);
    emitter.onCompletion(() ->
        emitterService
            .getSseSessions()
            .remove(username));

    emitter.onTimeout(emitter::complete);
    emitterService
        .getSseSessions()
        .put(username, emitter);

    messageRepository.setUserOnline(username);
    return emitter;
  }

  /**
   * Here we validate that the user is online,
   * and then he can get his pending messages.
   * When he gets them, we delete them.
   */
  public List<Message> getPendingMessage(String username)
  {
    User user = userService
        .getUserRepository()
        .findByUsername(username);
    List<Message> messages;

      messages = messageRepository.findPendingMessagesById(user.getUserId());
      messageRepository.deleteMessageFromPendingTable(user.getUserId());
      return messages;

  }
}