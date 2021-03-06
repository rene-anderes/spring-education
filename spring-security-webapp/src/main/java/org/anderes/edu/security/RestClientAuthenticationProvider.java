package org.anderes.edu.security;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

public class RestClientAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(RestClientAuthenticationProvider.class);
    
    @Autowired
    private UsersServiceClient service;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        final String username = authentication.getName();
        final Object credentials = authentication.getCredentials();
        
        if (!(credentials instanceof String)) {
            return null;
        }
        final String password = credentials.toString();
        
        try {
            final List<String> roles = service.getRolesForUser(username, password);
            logger.debug("Username: {}, Roles: {}", username, roles.stream().collect(Collectors.joining(",")));
            
            final List<GrantedAuthority> grantedAuthorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
        } catch (HttpClientErrorException e) {
            logger.warn(e.getMessage(), e);
            throw new BadCredentialsException(e.getMessage());
        } catch (HttpServerErrorException e) {
            logger.warn(e.getMessage(), e);
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
