/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.controllers;

import java.security.Principal;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 *
 * @author maja
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
