/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.dtp.framework.controllers;

import static com.mchange.v2.c3p0.impl.C3P0Defaults.user;
import hr.dtp.framework.model.ChatMessage;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 *
 * @author paz
 */
@Controller
public class ChatMessageController {
    
  private SimpMessagingTemplate template;
  
  @Autowired
  public ChatMessageController(SimpMessagingTemplate template) {
    this.template = template;
  }

  @MessageMapping("/chat")
  public void greeting(Message<Object> message, @Payload ChatMessage chatMessage) throws Exception {
      
      Principal principal = message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
      String authedSender = principal.getName();
      chatMessage.setSender(authedSender);   
      
      Logger.getLogger("ChatMessageController.java").log(Level.INFO,
                "Message :" + chatMessage.getMessage() + " to user : " + chatMessage.getReciever() +"from : " +chatMessage.getSender());
      
      template.convertAndSendToUser(chatMessage.getReciever(), "/queue/messages", chatMessage);
      
      
    }
}