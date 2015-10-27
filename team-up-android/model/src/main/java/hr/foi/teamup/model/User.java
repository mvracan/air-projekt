package hr.foi.teamup.model;

import java.io.Serializable;

/**
 *
 * Created by Tomislav Turek on 23.10.15..
 */
public class User implements Serializable {

    long id;
    String name;
    String surname;
    Credentials credentials;

    public User() {
    }

    public User(long id, String name, String surname, Credentials credentials) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.credentials = credentials;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
