/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.controllers;

import hr.foi.teamup.model.Location;
import hr.foi.teamup.model.Person;
import hr.foi.teamup.model.Team;
import hr.foi.teamup.model.TeamMessage;
import hr.foi.teamup.repositories.PersonRepository;
import hr.foi.teamup.repositories.TeamRepository;
import java.security.Principal;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
    TeamRepository teamRepository;
   
   
    
    @Autowired
    public TeamMessageController(SimpMessagingTemplate template, PersonRepository personRepository, TeamRepository teamRepository) {
        
    this.template = template;
    this.personRepository = personRepository;
    this.teamRepository = teamRepository;
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
    public void updateLocation(Message<Object> message, @Payload Location location){

        String authedSender = getSessionUsername(message);
        
        Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Update location for person" + authedSender);
        
        Person a = this.personRepository.findByCredentialsUsername(authedSender);
        
        Team t = this.teamRepository.findByActiveAndMembers_IdPerson(1, a.getIdPerson());
        Person lead = t.getCreator();
        
        double dist = location.distanceTo(lead.getLocation());
        
        Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Distance " + dist);
        
        if(t.getRadius() < location.distanceTo(lead.getLocation())) {
            
            a.setPanic(1);
            
            template.convertAndSendToUser(lead.getCredentials().getUsername(), "/queue/messages", a);
            
            if(equalsById(lead, a))
                template.convertAndSendToUser(a.getCredentials().getUsername(), "/queue/messages", a);
            
        }
        
        
        Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Person get from repo" + a.getName());
        
        a.setLocation(location);
        
        Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Going to save person" + a.getName());
        
        this.personRepository.save(a);
        
        
    }
    
    @MessageMapping("/team/{idTeam}/panic/{idPanicr}")
    public void panic(@DestinationVariable long idTeam, @DestinationVariable long idPanicr) {
        Team t = this.teamRepository.findByIdTeam(idTeam);
        Person lead = t.getCreator();
        Person panics = this.personRepository.findByIdPerson(idPanicr);
        panics.setPanic(1);
        this.personRepository.save(panics);
        Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "User " + panics.getName() + " panics, calling admin " + lead.getName());
        template.convertAndSendToUser(lead.getCredentials().getUsername(), "/queue/messages", panics);
    }
    
    public String getSessionUsername(Message<Object> message){
        
        Principal principal = message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
        return principal.getName();
        
    }
    
    
    @MessageMapping("/team/{idTeam}/calmUser")
    public void calmDownUser(Message<Object> message,@Payload String username,@DestinationVariable long idTeam){
        
        String authedSender = getSessionUsername(message);
        
        Person sender = this.personRepository.findByCredentialsUsername(authedSender);
        
        Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Authed user " + sender.getCredentials().getUsername() );
        
        Person panicUser = this.personRepository.findByCredentialsUsername(username);
        
        Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Calming down user " + panicUser.getCredentials().getUsername() );
        
        Team team = this.teamRepository.findByIdTeam(idTeam);
        
        Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Calming down user " + team.getCreator().getCredentials().getUsername());
        
        if(equalsById(team.getCreator(), sender)){
            
            panicUser.setPanic(0);
            this.personRepository.save(panicUser);
            
            Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "User is safe" );
            
        }
        else
            Logger.getLogger("MessageController.java").log(Logger.Level.INFO,
                "Not autherized for this operation" );

    }
    
    public boolean equalsById(Person lead, Person member){
        
        return ( lead.getIdPerson() == member.getIdPerson() );
         
        
    }
    
    
}
