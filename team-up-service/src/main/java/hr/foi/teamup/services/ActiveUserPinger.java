/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.services;
import hr.foi.teamup.model.Team;
import hr.foi.teamup.repositories.TeamRepository;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author paz
 */
public class ActiveUserPinger {
    
  private SimpMessagingTemplate template;
  private TeamRepository teamRepository;
  
  //TODO

  public ActiveUserPinger(SimpMessagingTemplate template, TeamRepository teamRepository) {
    this.template = template;
    this.teamRepository = teamRepository;
       
  }
  
  @Scheduled(fixedDelay = 2000)
  public void pingUsers() {
     Logger.getLogger("TeamUserPinger.java").log(Level.INFO,
                "Going to ping users");
    
  
    
    List<Team> Teams = this.teamRepository.findByActive(1);
    
    for(Team t: Teams){
        
    template.convertAndSend("/topic/group/" + t.getIdTeam() , t.getMembers());
     
    }
    
  }
    
}
