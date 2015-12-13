/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.dtp.framework.model;

/**
 *
 * @author paz
 */
public class ChatMessage {
    
    String sender;
    String reciever;
    
    Person message;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public Person getMessage() {
        return message;
    }

    public void setMessage(Person message) {
        this.message = message;
    }

    

    

    
    
    
    
}
