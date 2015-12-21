/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.services;

import hr.foi.teamup.model.Person;
import hr.foi.teamup.repositories.PersonRepository;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author paz
 */
@Service
public class PersonDetailsService implements UserDetailsService {
    
    private PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
            this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Person user = personRepository.findByCredentialsUsername(username);
            if (user == null) {
                    throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
            }
            return new PersonRepositoryUserDetails(user);
    }

    private final static class PersonRepositoryUserDetails extends Person implements UserDetails {

            private static final long serialVersionUID = 1L;

            private PersonRepositoryUserDetails(Person user) {
                    super(user);
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                    return getRoles();
            }

            @Override
            public String getUsername() {
                    return getCredentials().getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                    return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                    return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                    return true;
            }

            @Override
            public boolean isEnabled() {
                    return true;
            }

        @Override
        public String getPassword() {
            return getCredentials().getPassword();
        }

    }
    
}
