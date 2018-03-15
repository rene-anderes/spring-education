package org.anderes.edu.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
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

    @Bean
    @Override
    public UserDetailsManager userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("userEdit").password("$2a$10$k6KIBQh4uYy0agps9zDcbeDpQHwzvVEjEs1SF/bQtAXyIX3X7tUKq").roles("USER").build());
        manager.createUser(User.withUsername("user@delete").password("$2a$10$k6KIBQh4uYy0agps9zDcbeDpQHwzvVEjEs1SF/bQtAXyIX3X7tUKq").roles("USER").build());
        manager.createUser(User.withUsername("user").password("$2a$10$k6KIBQh4uYy0agps9zDcbeDpQHwzvVEjEs1SF/bQtAXyIX3X7tUKq").roles("USER", "ANONYMOUS").build());
        manager.createUser(User.withUsername("admin").password("$2a$10$k6KIBQh4uYy0agps9zDcbeDpQHwzvVEjEs1SF/bQtAXyIX3X7tUKq").roles("ADMIN", "USER", "ANONYMOUS").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
