/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.configurations;

import java.security.Principal;
import java.util.List;
import org.jboss.logging.Logger;
import org.springframework.context.annotation.*;
import org.springframework.context.event.EventListener;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 *
 * @author paz
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{
    private Object SimpMessageHeaderAccessor;
  
    
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/queue", "/topic","/group" );
    config.setApplicationDestinationPrefixes("/app");
    
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/chat", "/activeUsers" ,"/group");
    
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
        
  @EventListener
  public void handleSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent){
      
      Message message= sessionSubscribeEvent.getMessage();
      StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
      
      String dest = accessor.getDestination();
      
      StompCommand command = accessor.getCommand();
      
      
      Principal a =  sessionSubscribeEvent.getUser();
      String username = a.getName();
      
       Logger.getLogger("WebSocketConfig.java").log(Logger.Level.INFO,
                "Subscription for session for " + username + " command :" + command.name() +
                        " destination : " + dest);
       
       //TODO parse destination string if topic is group and add person to team repository
       //TODO add unsubscribe event
  }

}
