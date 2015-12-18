package hr.foi.teamup.model;

import java.io.Serializable;

/**
 *
 * Created by maja on 18.12.15..
 */
public class TeamMessage implements Serializable {

    String sender;
    String receiver;
    Person person;

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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
