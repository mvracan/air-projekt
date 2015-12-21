/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.controllers;

import hr.foi.teamup.model.TeamMessage;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 *
 * @author maja
 */
@Controller
public class TeamMessageController {
    private SimpMessagingTemplate template;
    
    @Autowired
    public TeamMessageController(SimpMessagingTemplate template) {
        
    this.template = template;
    
    }
    
    @MessageMapping("/team/{id}")
    public void sendToGroup( 
            @Payload TeamMessage teamMessage,
            @DestinationVariable long id) throws Exception {
        
         Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Sending back" + teamMessage.getMessage().getName());
        
        template.convertAndSend("/topic/team/" + id, teamMessage);
    
    } 
}
