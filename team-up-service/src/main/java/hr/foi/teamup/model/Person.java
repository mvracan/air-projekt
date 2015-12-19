/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Tomislav Turek
 */
@Entity
@Table(name="person")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="idPerson") 
public class Person implements Serializable {
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_person")
    long idPerson;
    
    @Column(name="name")
    String name;
    
    @Column(name="surname")
    String surname;
   
    
    @Embedded
    Credentials credentials;
    
    @Embedded
    Location location;
    
    @JsonIgnore
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private List<Team> creatorOfGroups;
    
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "members")
    @JsonIgnore
    private  List<Team>  memberOfGroups;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "person_role", joinColumns = { @JoinColumn(name = "id_person") },
            inverseJoinColumns = { @JoinColumn(name = "id_role") })
    Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

       public Person() {
    }

    public Person(Person person) {
        super();
        this.idPerson = person.getIdPerson();
        this.name = person.getName();
        this.surname = person.getSurname();
        this.credentials = person.getCredentials();
    }

 
    // String token

    public long getIdPerson() {
        return idPerson;
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
