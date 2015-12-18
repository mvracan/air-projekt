/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.teamup.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

/**
 *
 * @author maja
 */
@Component
@EnableWebSecurity
public class TeamSecurity extends WebSecurityConfigurerAdapter{

    @Autowired
    PersonDetailsService users;

    @Autowired
    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
          public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

              /*
            auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select * from person where username=?")
                .authoritiesByUsernameQuery("SELECT p.username, r.name " +
                  "FROM person p  " + 
                  "LEFT OUTER JOIN person_role ON p.id_person = person_role.id_person " +
                  "LEFT OUTER JOIN role r ON person_role.id_role = r.id_role" +
                        " WHERE p.username=?");
                      */

               auth.userDetailsService(users);

          }	

      @Override
      protected void configure(HttpSecurity http) throws Exception {

          http.csrf().disable().
                  authorizeRequests()
                  .antMatchers("/login/**").permitAll()
                  .antMatchers("/person/**").permitAll()
                  .antMatchers("/team/**").permitAll()
                  .anyRequest().authenticated().and().formLogin().successHandler(authenticationSuccessHandler);


  }
}