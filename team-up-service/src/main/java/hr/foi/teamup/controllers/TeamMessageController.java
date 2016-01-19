/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.controllers;

import hr.foi.teamup.model.Location;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.model.TeamMessage;
import hr.foi.teamup.repositories.PersonRepository;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

/**
 *
 * @author maja
 */
@Controller
public class TeamMessageController {
    private SimpMessagingTemplate template;
    
    PersonRepository personRepository;

   
   
    
    @Autowired
    public TeamMessageController(SimpMessagingTemplate template, PersonRepository personRepository) {
        
    this.template = template;
    this.personRepository = personRepository;
    
    }
    
    @MessageMapping("/team/{id}")
    public void sendToGroup( 
            @Payload TeamMessage teamMessage,
            @DestinationVariable long id) throws Exception {
        
         Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Sending back" + teamMessage.getMessage().getName());
        
        template.convertAndSend("/topic/team/" + id, teamMessage);
    
    } 
    
    @MessageMapping("/updateLocation")
    public void updateLocation(@Payload Location location){
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        String username = auth.getName();
        
        Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Update location for person" + username);
        
        Person a = this.personRepository.findByCredentialsUsername(username);
        
        Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Person get from repo" + a.getName());
        
        a.setLocation(location);
        
        Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Going to save person" + a.getName());
        
        this.personRepository.save(a);
        
        
    }
    
}
