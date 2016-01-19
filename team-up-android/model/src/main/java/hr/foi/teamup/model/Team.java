package hr.foi.teamup.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by Tomislav Turek on 31.10.15..
 */
public class Team implements Serializable {

    long idTeam;
    String name;
    String teamDesc;
    String password;
    double radius;
    String nfcCode;
    Person creator;
    List<Person> members;
    int active;


    public Team(long IdTeam, String name, String teamDesc, String password, double radius, String nfcCode,
                Person creator,
                List<Person> members) {
        this.idTeam= idTeam;
        this.name = name;
        this.teamDesc = teamDesc;
        this.password = password;
        this.radius = radius;
        this.nfcCode = nfcCode;
        this.creator = creator;
        this.members = members;

    }

    public long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(long idTeam) {
        this.idTeam = idTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamDesc() {
        return teamDesc;
    }

    public void setTeamDesc(String teamDesc) {
        this.teamDesc = teamDesc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getNfcCode() {
        return nfcCode;
    }

    public void setNfcCode(String nfcCode) {
        this.nfcCode = nfcCode;
    }

    public Person getCreator() { return creator; }

    public void setCreator(Person creator) { this.creator = creator; }

    public List<Person> getMembers() { return members; }

    public void setMembers(List<Person> members) { this.members = members; }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return this.name + " " + this.teamDesc + " " + this.password;
    }
}
