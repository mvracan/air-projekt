/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.controllers;

import hr.foi.teamup.model.Team;
import hr.foi.teamup.repositories.TeamRepository;
import java.util.List;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     
    @Autowired
    public TeamController(TeamRepository groupRepository) {
         
        this.teamRepository = groupRepository;
        
    }
     
     /**
     * gets all groups from database
     * @return all groups in json format with HTTP 200
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Team>> retrieveAll() {
        Logger.getLogger("GroupController.java").log(Logger.Level.INFO,
                "GET on /group -- retrieving full list of groups");
        return new ResponseEntity(this.teamRepository.findAll(), HttpStatus.OK);
    }
    
    
}
