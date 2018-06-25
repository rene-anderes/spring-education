package org.anderes.edu.security;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * ------ Testing ----------------------------
 * Konfiguration des Spring Security Context's
 * 
 * @author René Anderes
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityTestConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder, AuthenticationProvider authenticationProvider) throws Exception {
        builder.authenticationProvider(authenticationProvider);
    }
    
    @Bean  @Profile("!mock")
    public UsersServiceClient usersService() {
        return new UsersServiceClient();
    }
    
    @Bean @Profile("mock")
    public UsersServiceClient usersMockService() {
        return Mockito.mock(UsersServiceClient.class);
    }
    
    @Bean 
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }
    
    @Bean @Profile("alternativ")
    public AuthenticationProvider authenticationProviderAlt() {
        return new RestClientAuthenticationProvider();
    }
    
    @Bean("UsersRestUrl")
    public String usersRestUrlTesting() {
        return "/users/token";
    }
}
