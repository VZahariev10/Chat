package com.example.chatSSE.controllerTestNG;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertNotNull;

import com.example.chatSSE.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SpringBootTest
@WebAppConfiguration
@ContextConfiguration
@Transactional
public class MessageControllerIntegrationTestNG extends AbstractTestNGSpringContextTests
{
  private MockMvc      mockMvc;
  private ObjectMapper objectMapper;
  @Autowired
  WebApplicationContext wac;

  @BeforeMethod
  public void setup()
  {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(this.wac)
        .build();
    this.objectMapper = new ObjectMapper();
  }

  @Test
  void test_wires()
  {
    assertNotNull(wac);
    assertNotNull(objectMapper);
    assertNotNull(mockMvc);
  }

  @Test
  public void send_message_should_send_message() throws Exception
  {

    Message message = new Message();
    message.setSenderId(135);
    message.setReceiverId(151);
    message.setMessage("bla bla bla");

    mockMvc
        .perform(put("/api/v1/message/send")
            .with(SecurityMockMvcRequestPostProcessors.httpBasic("Allan", "12345"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(message)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void send_too_long_message_should_not_send_message() throws Exception
  {
    Message message = new Message();
    message.setSenderId(4);
    message.setReceiverId(3);
    message.setMessage("blablablaablablablaablablablaablablablaablablablaa"
        + "             blablablaablablablaablablablaablablablaablablablaa"
        + "             blablablaablablablaablablablaablablablaablablablaa"
        + "             blablablaablablablaablablablaablablablaablablablaa"
        + "             blablablaablablablaablablablaablablablaablablablaa"
        + "             blablablaablablablaablablablaablablablaablablablaaa");

    mockMvc
        .perform(put("/api/v1/message/send")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(message)))
        .andDo(print())
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void send_message_with_null_id_should_not_send_message() throws Exception
  {
    Message message = new Message();
    message.setSenderId(null);
    message.setReceiverId(null);
    message.setMessage("bla bla bla");

    mockMvc
        .perform(put("/api/v1/message/send")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(message)))
        .andDo(print())
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void send_message_with_negative_id_should_not_send_message() throws Exception
  {
    Message message = new Message();
    message.setSenderId(-3);
    message.setReceiverId(-5);
    message.setMessage("bla bla bla");

    mockMvc
        .perform(put("/api/v1/message/send")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(message)))
        .andDo(print())
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void user_login_should_log_in_user() throws Exception
  {
    mockMvc
        .perform(get("/api/v1/message/subscribe")
            .with(SecurityMockMvcRequestPostProcessors.httpBasic("John", "12345678")))
        .andDo(print())
        .andExpect(status().isOk());
  }


  @Test
  public void user_getPendingMessage_should_get_current_user_messages() throws Exception
  {


    mockMvc
        .perform(get("/api/v1/message/getPendingMessage")
            .with(SecurityMockMvcRequestPostProcessors.httpBasic("Mick", "password")))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void aSyncTest() throws Exception
  {
    MvcResult result = mockMvc
      .perform(get("/api/v1/message/subscribe")
          .with(SecurityMockMvcRequestPostProcessors.httpBasic("Mick", "password")))
      .andDo(print())
      .andExpect(MockMvcResultMatchers
          .request()
          .asyncStarted())
      .andExpect(status().isOk())
      .andReturn();
    try {
      mockMvc
          .perform(asyncDispatch(result))
          .andDo(MockMvcResultHandlers.print())
          .andExpect(status().isOk());
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    Message message = new Message("asdasd", 135, 151);
    mockMvc
        .perform(put("/api/v1/message/send")
            .with(SecurityMockMvcRequestPostProcessors.httpBasic("Allan", "12345"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(message)))
        .andExpect(status().isOk());
  }
}