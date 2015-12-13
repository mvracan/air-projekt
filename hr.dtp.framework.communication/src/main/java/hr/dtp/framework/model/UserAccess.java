/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.dtp.framework.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author paz
 */
public class UserAccess {
    
    private AtomicLong lastAccess = new AtomicLong(System.currentTimeMillis());
    
    
    
     public void mark() {
      lastAccess.set(System.currentTimeMillis());
    }
    
    public long lastAccess() {
      return lastAccess.get();
    }
    
}
