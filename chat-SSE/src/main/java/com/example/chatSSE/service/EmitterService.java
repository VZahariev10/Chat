package com.example.chatSSE.service;

import com.example.chatSSE.model.Message;
import com.example.chatSSE.model.User;
import com.example.chatSSE.repository.MessageRepository;
import java.io.IOException;
import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A Class that helps us work with a Sse Emitter
 */
@Service
public class EmitterService
{


  private final MessageRepository messageRepository;

  /**
   * A Thread safe Map in which we keep the emitters as the values,
   * and the Usernames as the keys.
   */
  private final static ConcurrentHashMap<String, SseEmitter> sseSessions = new ConcurrentHashMap<>();

  public EmitterService(MessageRepository messageRepository)
  {
    this.messageRepository = messageRepository;
  }

  /**
   * In this method, we send a message by the receiver's emitter,
   * so if he is online,
   * and if he listens to his emitter,
   * he will get the message after 2 seconds(configurable).
   * Otherwise, the message status will be changed to pending.
   */
  public SseEmitter messageSend(Message message, Principal principal)
  {
    try {
      User receiver = messageRepository.getReceiver(message.getReceiverId());
      User sender = messageRepository.getSender(message.getSenderId());
      if (sender
          .getUserName()
          .equals(principal.getName())) {

        if (sender.getIsOnline() == 1) {

          if (sseSessions.containsKey(receiver.getUserName())) {

            try {
              sseSessions
                  .get(receiver.getUserName())
                  .send(sender.getUserName() + ": " + message.getMessage(),
                      MediaType.APPLICATION_JSON);
            }
            catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
          else {
            messageRepository.updatePending(message);
          }
        }
      }
      else {
        throw new RuntimeException("Invalid Sender ID");
      }
    }
    catch (EmptyResultDataAccessException erdax) {
      System.out.println("\nInvalid receiver or sender ID!");
      return null;
    }
    return null;
  }

  public ConcurrentHashMap<String, SseEmitter> getSseSessions()
  {
    return sseSessions;
  }
}