package org.anderes.web.security;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Inject
    private JwtTokenValidator jwtTokenValidator;

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        
        final JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        final String token = jwtAuthenticationToken.getToken();

        Optional<UserDetails> parsedUser = jwtTokenValidator.parseToken(token);
        final UserDetails user = parsedUser.orElseThrow(() -> new BadCredentialsException("JWT token is not valid"));
        
        return user;
    }

}
