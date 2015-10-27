/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Tomislav Turek
 */
@Entity
@Table(name="user")
public class User implements Serializable {
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_user")
    long idUser;
    
    @Column(name="name")
    String name;
    
    @Column(name="surname")
    String surname;
   
    
    @Embedded
    Credentials credentials;
    
    @Embedded
    Location location;
    
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY,
			cascade = CascadeType.ALL)
    private List<Group> creatorOfGroups;
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "members")
    @JsonBackReference
    private  List<Group> memberOfGroups;

    // String token

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Group> getCreatorOfGroups() {
        return creatorOfGroups;
    }

    public void setCreatorOfGroups(List<Group> creatorOfGroups) {
        this.creatorOfGroups = creatorOfGroups;
    }

    public List<Group> getMemberOfGroups() {
        return memberOfGroups;
    }

    public void setMemberOfGroups(List<Group> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
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
    
    @Override
    public String toString() {
        return this.name + " " + this.surname;
    }
    
}
