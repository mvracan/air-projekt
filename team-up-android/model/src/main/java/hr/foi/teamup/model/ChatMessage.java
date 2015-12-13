package hr.foi.teamup.model;

/**
 * Created by paz on 13.12.15..
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
