package hr.foi.teamup.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by Tomislav Turek on 23.10.15..
 */
public class Person implements Serializable {

    long idPerson;
    String name;
    String surname;
    Credentials credentials;
    Location location;

    private List<Team> creatorOfGroups;
    private List<Team> memberOfGroups;

    public Person() {
    }

    public Person(long idPerson, String name, String surname, Credentials credentials, Location location) {
        this.idPerson = idPerson;
        this.name = name;
        this.surname = surname;
        this.credentials = credentials;
        this.location = location;
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

    public long getIdPerson() {
        return idPerson;
    }

    public List<Team> getCreatorOfGroups() {
        return creatorOfGroups;
    }

    public void setCreatorOfGroups(List<Team> creatorOfGroups) {
        this.creatorOfGroups = creatorOfGroups;
    }

    public List<Team> getMemberOfGroups() {
        return memberOfGroups;
    }

    public void setMemberOfGroups(List<Team> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }

    public void setIdPerson(long idPerson) {
        this.idPerson = idPerson;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return this.name + " " + this.surname;
    }
}
