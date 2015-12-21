/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.configurations;

import hr.foi.teamup.repositories.TeamRepository;
import hr.foi.teamup.services.ActiveUserPinger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;


/**
 *
 * @author maja
 */
@Configuration
@EnableScheduling
public class TeamSchedulingConfigurer implements SchedulingConfigurer {
    @Bean
  public ThreadPoolTaskScheduler taskScheduler() {
     return new ThreadPoolTaskScheduler();
  }
  
  /**
   * This is setting up a scheduled bean which will see which users are active
   */
  @Bean
  @Autowired
  public ActiveUserPinger activeUserPinger(SimpMessagingTemplate template, TeamRepository activeUserService) {
    return new ActiveUserPinger(template, activeUserService);
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setTaskScheduler(taskScheduler());
  }
}
