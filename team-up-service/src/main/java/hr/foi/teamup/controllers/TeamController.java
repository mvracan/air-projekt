/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.controllers;

import hr.foi.teamup.model.Person;
import hr.foi.teamup.model.Team;
import hr.foi.teamup.repositories.PersonRepository;
import hr.foi.teamup.repositories.TeamRepository;
import java.util.List;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author paz
 */
@RestController
@RequestMapping(value = "/team")
public class TeamController {
    
    TeamRepository teamRepository;
    PersonRepository personRepository;
    
    @Autowired
    public TeamController(TeamRepository groupRepository, PersonRepository personRepository) {
         
        this.teamRepository = groupRepository;
        this.personRepository = personRepository;
    }
     
     /**
     * gets all groups from database
     * @return all groups in json format with HTTP 200
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Team>> retrieveAll() {
        Logger.getLogger("TeamController.java").log(Logger.Level.INFO,
                "GET on /team/ -- retrieving full list of groups");
        return new ResponseEntity(this.teamRepository.findAll(), HttpStatus.OK);
    }
    
    @RequestMapping(value="/create", method=RequestMethod.POST)
    public ResponseEntity<Team> createTeam(@RequestBody Team t){
        Logger.getLogger("TeamController.java").log(Logger.Level.INFO,
                "POST on /team/create -- creating team " + t.toString());
        
        Team created = teamRepository.save(t);
        if(created != null){
            Logger.getLogger("TeamController.java").log(Logger.Level.INFO,
                "Successfully created team " + created.toString());
            return new ResponseEntity(created,HttpStatus.OK);
        }
        else {
            Logger.getLogger("TeamController.java").log(Logger.Level.INFO,
                "Failed to create team " + t.toString());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTeam (@PathVariable long id){
        Logger.getLogger("TeamController.java").log(Logger.Level.INFO,
                "DELETE on /team/" + id);
        Team found = teamRepository.findByIdTeam(id);
        
        if(found != null){
            Logger.getLogger("TeamController.java").log(Logger.Level.INFO,
                    "Successfully found team " + found.toString());
            teamRepository.delete(found);
            return new ResponseEntity(HttpStatus.OK);
        }
        else{
            Logger.getLogger("TeamController.java").log(Logger.Level.INFO,
                "No team found for " + id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value="/{idTeam}/person/{idPerson}", method=RequestMethod.POST)
    public ResponseEntity addPeopleToTeam (@PathVariable long idTeam,@PathVariable long idPerson){
        Logger.getLogger("TeamController.java").log(Logger.Level.INFO, "POST on /team/" + idTeam 
        + "/person/" + idPerson);
        Team foundTeam=teamRepository.findByIdTeam(idTeam);
        Person foundPerson=personRepository.findByIdPerson(idPerson);
        
        return new ResponseEntity(HttpStatus.OK);
    }
}
