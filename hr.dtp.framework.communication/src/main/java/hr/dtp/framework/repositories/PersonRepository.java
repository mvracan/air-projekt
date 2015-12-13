package hr.dtp.framework.repositories;
import hr.dtp.framework.model.Person;
import java.util.List;
import javax.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Tomislav Turek
 */
@Repository
@Table(name="person")
public interface PersonRepository extends CrudRepository<Person, String> {
    
    public Person findByIdPerson(long id);
    public List<Person> findByIdPersonIn(List<Long> idPerson);
    public Person findByCredentialsUsername(String username);
    
}
