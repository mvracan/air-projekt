/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.repositories;


import hr.foi.teamup.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author paz
 */
public interface GroupRepository extends JpaRepository<Group, String> {

}