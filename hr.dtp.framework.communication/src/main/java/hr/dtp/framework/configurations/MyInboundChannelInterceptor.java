/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.dtp.framework.configurations;

import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

/**
 *
 * @author paz
 */
public class MyInboundChannelInterceptor extends ChannelInterceptorAdapter {

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    StompCommand command = accessor.getCommand();
      Principal User = accessor.getUser();
       Logger.getLogger("InboundInterceptor.java").log(Level.INFO,
                "#User sending" + User.getName() + "channel "+ channel.toString()
        );
       
     
       
       
    if(command == StompCommand.MESSAGE || command == StompCommand.SUBSCRIBE || command == StompCommand.SEND){
    
        String destination = accessor.getDestination();
        if(destination != null)
            Logger.getLogger("InboundInterceptor.java").log(Level.INFO,
                "Destination is" + destination 
        );
       
        
        Logger.getLogger("OutboundInterceptor.java").log(Level.INFO,
                "Command is" + command.toString() 
        );
        
        
      
        
       User = accessor.getUser();
    
     String session= accessor.getSessionId();
    
     Logger.getLogger("OutboundInterceptor.java").log(Level.INFO,
                "User sending message  with command  user with session" + command.toString() +
                        " , "+ User.getName()+ " , " + session);
    }
    
    return message;
  }
  
}