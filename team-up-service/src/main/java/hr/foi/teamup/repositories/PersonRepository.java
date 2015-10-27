/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.repositories;

import hr.foi.teamup.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tomislav Turek
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
    
    public Person findByUsername(String username);
    public Person findById(long id);
    
}
