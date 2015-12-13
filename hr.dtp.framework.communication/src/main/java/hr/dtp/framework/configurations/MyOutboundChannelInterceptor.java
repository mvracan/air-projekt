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
public class MyOutboundChannelInterceptor extends ChannelInterceptorAdapter {

  @Override
  public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    StompCommand command = accessor.getCommand();
      Principal User = accessor.getUser();
      
       Logger.getLogger("OutboundInterceptor.java").log(Level.INFO,
                "#Message send to user is" + User.getName() + channel.toString()
        );
       
       
    if(command == StompCommand.MESSAGE || command == StompCommand.SUBSCRIBE || command == StompCommand.SEND){
    
        Logger.getLogger("OutboundInterceptor.java").log(Level.INFO,
                "Command is" + command.toString() 
        );
        
        //Principal User = accessor.getUser();
    
    String session= accessor.getSessionId();
    
     Logger.getLogger("OutboundInterceptor.java").log(Level.INFO,
                "Sending with command to user with session" + command.toString() +
                        " , "+ User.getName()+ " , " + session);
    }
    
  
  }
}