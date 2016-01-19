/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.configurations;

import hr.foi.teamup.repositories.PersonRepository;
import hr.foi.teamup.repositories.TeamRepository;
import java.security.Principal;
import java.util.List;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 *
 * @author paz
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{
    
    private Object SimpMessageHeaderAccessor;
    @Autowired
    private TeamRepository teamRepository ;
    @Autowired
    private PersonRepository personRepository;

    public WebSocketConfig() {
        
    }

    @Autowired
    WebSocketConfig(TeamRepository teamRepository, PersonRepository personRepository){
        this.teamRepository = teamRepository;
        this.personRepository = personRepository;
    }
  
    
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/queue", "/topic" );
    config.setApplicationDestinationPrefixes("/app");
    
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint( "/team");
    
  }
  
  @Override
  public void configureClientInboundChannel(ChannelRegistration channelRegistration) {
    
  }

  @Override
  public void configureClientOutboundChannel(ChannelRegistration channelRegistration) {
      
  }

    /**
     *
     * @param converters
     * @return
     */
    @Override
  public boolean configureMessageConverters(List<MessageConverter> converters) {
      /*
      converters.add((MessageConverter) new FormHttpMessageConverter());
      converters.add((MessageConverter) new StringHttpMessageConverter());
      converters.add((MessageConverter) new MappingJackson2HttpMessageConverter());
              */
    return true;
  }
  

  
  @EventListener
 	private void handleSessionConnected(SessionConnectEvent event) {
 		SimpMessageHeaderAccessor headers;
                
                Principal a =  event.getUser();
 		String username = a.getName();
                
                Logger.getLogger("WebSocketConfig.java").log(Logger.Level.INFO,
                "Created session for " + username);

 	
 	}
    /*
  @EventListener
  public void handleSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent){
      
      Message message= sessionSubscribeEvent.getMessage();
      StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
      
      String dest = accessor.getDestination();
      
      StompCommand command = accessor.getCommand();
      
      
      Principal a =  sessionSubscribeEvent.getUser();
      String username = a.getName();
      
       Logger.getLogger("WebSocketConfig.java").log(Logger.Level.INFO,
                "username : " + username);
      
      
      String regex="^\\/topic\\/team\\/(\\d)+$";
      Pattern pat= Pattern.compile(regex);
      
      
      if(pat.matcher(dest).matches()){
          
          Logger.getLogger("WebSocketConfig.java").log(Logger.Level.INFO,
                "ITUSAM JEBEM TI" + dest);
          String  idTeam=null;
          String[] parts = dest.split("/");
          for(int i=0;i<parts.length;i++){
               Logger.getLogger("WebSocketConfig.java").log(Logger.Level.INFO,
                parts[i] +" : "+ i);
                idTeam=parts[i];
          }
          Logger.getLogger("WebSocketConfig.java").log(Logger.Level.INFO,
                "ITUSAM JEBEM TI 2" + idTeam);
              
          
           
           Team foundTeam=teamRepository.findByIdTeam(Long.parseLong(idTeam));
           Person foundPerson=personRepository.findByCredentialsUsername(username);
           Logger.getLogger("WebSocketConfig.java").log(Logger.Level.INFO,
                "ITUSAM JEBEM TI 2 -" + foundPerson.getIdPerson());
           
           foundTeam.getMembers().add(foundPerson);
           this.teamRepository.save(foundTeam);
           
     
            Logger.getLogger("WebSocketConfig.java").log(Logger.Level.INFO,
                "ITUSAM JEBEM TI 2" + foundPerson.getIdPerson());
           
          
      }
      
      
       
       
  }
  */
}
