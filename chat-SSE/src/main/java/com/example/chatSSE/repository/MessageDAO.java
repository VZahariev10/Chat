package com.example.chatSSE.repository;

import com.example.chatSSE.model.Message;
import com.example.chatSSE.model.User;

import java.util.List;

public interface MessageDAO
{
  User getSender(Integer id);

  User getReceiver(Integer id);

  void updatePending(Message message);

  void deleteMessageFromPendingTable(Integer id);

  void logOut(String username);


  void setUserOnline(String username);

  List<Message> findPendingMessagesById(Integer id);
}