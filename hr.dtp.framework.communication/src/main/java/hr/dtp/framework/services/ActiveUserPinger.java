/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.dtp.framework.services;
import hr.dtp.framework.repositories.PersonRepository;
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
  private ActiveUserService activeUserService;

  public ActiveUserPinger(SimpMessagingTemplate template, ActiveUserService activeUserService) {
    this.template = template;
    this.activeUserService = activeUserService;   
  }
  
  @Scheduled(fixedDelay = 2000)
  public void pingUsers() {
     Logger.getLogger("ActiveUserPinger.java").log(Level.INFO,
                "Going to ping users");
    Set<String> activeUsers = activeUserService.getActiveUsers();
    
     Logger.getLogger("ActiveUserPinger.java").log(Level.INFO,
                "Active user" + activeUsers.toString() );
     
    template.convertAndSend("/topic/active", activeUsers );
  }
    
}
