/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.dtp.framework.configurations;

import hr.dtp.framework.services.ActiveUserService;
import java.security.Principal;
import java.util.List;
import org.jboss.logging.Logger;
import org.springframework.context.annotation.*;
import org.springframework.context.event.EventListener;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
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
     // channelRegistration.setInterceptors(new MyInboundChannelInterceptor());
  }

  @Override
  public void configureClientOutboundChannel(ChannelRegistration channelRegistration) {
      //channelRegistration.setInterceptors(new MyOutboundChannelInterceptor());
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
  
 @Bean
  public ActiveUserService activeUserService() {
    return new ActiveUserService();
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
      
      Principal a =  sessionSubscribeEvent.getUser();
      String username = a.getName();
      
       Logger.getLogger("WebSocketConfig.java").log(Logger.Level.INFO,
                "Subscription for session for " + username);
      
  }

}
