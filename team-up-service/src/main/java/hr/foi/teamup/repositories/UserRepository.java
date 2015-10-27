/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.repositories;

import hr.foi.teamup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tomislav Turek
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    public User findByUsername(String username);
    public User findById(long id);
    
}
