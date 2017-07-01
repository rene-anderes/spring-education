package org.anderes.edu.security.jwt;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.anderes.edu.security.jwt.exception.JwtTokenMalformedException;
import org.anderes.edu.security.jwt.model.AuthenticatedUser;
import org.anderes.edu.security.jwt.model.JwtAuthenticationToken;
import org.anderes.edu.security.jwt.transfer.JwtUserDto;
import org.anderes.edu.security.jwt.util.JwtTokenValidator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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
        
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        Optional<JwtUserDto> parsedUser = jwtTokenValidator.parseToken(token);

        if (!parsedUser.isPresent()) {
            throw new JwtTokenMalformedException("JWT token is not valid");
        }

        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(parsedUser.get().getRolesAsCommaSeparatedString());

        return new AuthenticatedUser(parsedUser.get().getId(), parsedUser.get().getUsername(), token, authorityList);
    }

}
