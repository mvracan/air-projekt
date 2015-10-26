/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.model;
import java.io.Serializable;
import javax.persistence.Embeddable;
/**
 *
 * @author Tomislav Turek
 */
@Embeddable
public class Credentials implements Serializable {
    
    String username;
    String password;

    public Credentials() {
    }

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        return this.username + " " + this.password;
    }
    
}
