/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.dtp.framework.controllers;

import hr.dtp.framework.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 *
 * @author paz
 */
@Controller
public class GroupMessageController {
    
    private SimpMessagingTemplate template;
    
    @Autowired
    public GroupMessageController(SimpMessagingTemplate template) {
        
    this.template = template;
    
    }
    
    @MessageMapping("/group/{id}")
    public void sendToGroup( 
            @Payload ChatMessage chatMessage,
            @DestinationVariable long id) throws Exception {
        
        template.convertAndSend("/topic/group/" + id, chatMessage);
    
    }   
    
}
