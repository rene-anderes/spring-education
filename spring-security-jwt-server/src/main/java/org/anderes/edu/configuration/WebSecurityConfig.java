package org.anderes.edu.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()                                                               
                .antMatchers("/users/token/**").hasAnyRole("USER")
                .antMatchers("/users/*").hasRole("ADMIN")
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @SuppressWarnings("deprecation")
    @Bean
    public UserDetailsManager userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder().username("userEdit").password("password").roles("USER").build());
        manager.createUser(User.withDefaultPasswordEncoder().username("user@delete").password("password").roles("USER").build());
        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER", "ANONYMOUS").build());
        manager.createUser(User.withDefaultPasswordEncoder().username("admin").password("password").roles("ADMIN", "USER", "ANONYMOUS").build());
        return manager;
    }

}
