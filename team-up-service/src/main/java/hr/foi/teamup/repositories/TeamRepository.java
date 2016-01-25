/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.repositories;


import hr.foi.teamup.model.Team;
import java.util.List;
import javax.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author paz
 */
@Repository
@Table(name="team")
public interface TeamRepository extends JpaRepository<Team, String> {
    
    public Team findByIdTeam( long idTeam);
    public List<Team> findByMembers_IdPerson(long id);
    public List<Team> findByActive(long active);
    public Team findByActiveAndMembers_IdPerson(long active,long id);
    public Team findByPassword(String password);
}
