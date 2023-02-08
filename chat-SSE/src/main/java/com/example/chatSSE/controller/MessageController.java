package com.example.chatSSE.controller;

import com.example.chatSSE.model.Message;
import com.example.chatSSE.service.EmitterService;
import com.example.chatSSE.service.MessageService;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A Class that helps us request and execute HTTP Methods
 */
@RequestMapping("/api/v1/message")
@RestController
@EnableAsync
public class MessageController
{
  private final EmitterService emitterService;
  private final MessageService messageService;

  public MessageController(EmitterService emitterService, MessageService messageService)
  {
    this.emitterService = emitterService;
    this.messageService = messageService;
  }

  /**
   * A Http PUT method that provides us with the option of sending a message
   */
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("send")
  public ResponseBodyEmitter messageSend(@Valid @RequestBody Message message, Principal principal)
  {
    return emitterService.messageSend(message, principal);
  }

  /**
   * A Http GET method that provides us with the option of
   * getting all pending messages of the user
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("getPendingMessage")
  public List<Message> getPendingMessage(@AuthenticationPrincipal UserDetails userDetails)
  {
    return messageService.getPendingMessage(userDetails.getUsername());
  }

  /**
   * A Http GET method that provides us with the option of subscribing to an emitter.
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("subscribe")
  public CompletableFuture<SseEmitter> subscribe(@AuthenticationPrincipal UserDetails userDetails)
  {
    return CompletableFuture.completedFuture(messageService.subscribe(userDetails.getUsername()));
  }

  /**
   * A Http GET method that provides us with the option of logging out
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("logOut")
  public String logOut(@AuthenticationPrincipal UserDetails userDetails)
  {
    return messageService.logOut(userDetails);
  }
}