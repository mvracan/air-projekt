/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.model;

/**
 *
 * @author paz
 */
public class TeamMessage {
    
    String sender;
    String receiver;
    
    Person message;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

   
    public Person getMessage() {
        return message;
    }

    public void setMessage(Person message) {
        this.message = message;
    }

    

    

    
    
    
    
}
