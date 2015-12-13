/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.dtp.framework.services;

import java.util.concurrent.atomic.AtomicLong;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import hr.dtp.framework.model.Person;
import hr.dtp.framework.model.UserAccess;
import java.util.Set;

/**
 *
 * @author paz
 */
public class ActiveUserService {
    
     private LoadingCache< String, UserAccess> statsByUser = CacheBuilder.newBuilder().build(new CacheLoader< String, UserAccess>() {

    @Override
    public UserAccess load(String key) throws Exception {
      return new UserAccess();
    }
    
  });
  
    public void mark(String username){
        statsByUser.getUnchecked(username).mark();
    }
     
    
    public Set<String> getActiveUsers(){
        
     Set<String> activeUsers = Sets.newTreeSet();
     
     for(String user : statsByUser.asMap().keySet()){
         
         if ((System.currentTimeMillis() - statsByUser.getUnchecked(user).lastAccess()) < 5000) 
                activeUsers.add(user);
              
     }
     
     
      return activeUsers;
        
    }
   
}
