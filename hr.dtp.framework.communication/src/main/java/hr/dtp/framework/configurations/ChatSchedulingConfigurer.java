/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.dtp.framework.configurations;

import hr.dtp.framework.services.ActiveUserPinger;
import hr.dtp.framework.services.ActiveUserService;
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
 * @author paz
 */
@Configuration
@EnableScheduling
public class ChatSchedulingConfigurer implements SchedulingConfigurer{
    
 @Bean
  public ThreadPoolTaskScheduler taskScheduler() {
     return new ThreadPoolTaskScheduler();
  }
  
  /**
   * This is setting up a scheduled bean which will see which users are active
   */
  @Bean
  @Autowired
  public ActiveUserPinger activeUserPinger(SimpMessagingTemplate template, ActiveUserService activeUserService) {
    return new ActiveUserPinger(template, activeUserService);
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setTaskScheduler(taskScheduler());
  }
    
}
