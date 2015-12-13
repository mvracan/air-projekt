/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.dtp.framework.controllers;

import hr.dtp.framework.model.ChatMessage;
import org.springframework.messaging.Message;
import hr.dtp.framework.services.ActiveUserService;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 *
 * @author paz
 */
@Controller
public class ActiveUserController {
    
    private ActiveUserService activeUserService;
    
    @Autowired
     public ActiveUserController(ActiveUserService activeUserService) {
        this.activeUserService = activeUserService;
     }
     
    @MessageMapping("/activeUsers")
    public void activeUsers(Message<Object> message) {    
        
         Logger.getLogger("ActiveUserController.java").log(Level.INFO,
                "Here" );
        
        Principal user = message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
        
        Logger.getLogger("ActiveUserController.java").log(Level.INFO,
                "Going to save userr" + user.getName());
          
        activeUserService.mark(user.getName());
        
  }
    
}
