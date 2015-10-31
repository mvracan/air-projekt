package hr.foi.teamup.model;

import java.io.Serializable;

/**
 *
 * Created by Tomislav Turek on 23.10.15..
 */
public class Person implements Serializable {

    long idPerson;
    String name;
    String surname;
    Credentials credentials;

    public Person() {
    }

    public Person(long idPerson, String name, String surname, Credentials credentials) {
        this.idPerson = idPerson;
        this.name = name;
        this.surname = surname;
        this.credentials = credentials;
    }

    public long getidPerson() {
        return idPerson;
    }

    public void setidPerson(long idPerson) {
        this.idPerson = idPerson;
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
