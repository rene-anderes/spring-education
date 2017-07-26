package org.anderes.edu.security.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.anderes.edu.security.jwt.transfer.JwtUserDto;
import org.anderes.edu.security.jwt.util.JwtTokenGenerator;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class LoginJwtController {

    @Inject
    private AuthenticationFacade authenticationFacade; 
    @Inject
    private JwtTokenGenerator jwtTokenGenerator;
    
    @RequestMapping(value="token", method = POST, produces = { APPLICATION_JSON_UTF8_VALUE })
    public HttpEntity<JwtToken> getToken() {
        
        Optional<Authentication> authentication = authenticationFacade.getAuthentication();
        if (authentication.isPresent()) {
            final Collection<String> roles = authentication.get().getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
            final String userName = authentication.get().getName();
            JwtUserDto user = new JwtUserDto(123L, userName, roles);
            final String token = jwtTokenGenerator.generateToken(user);
            return ResponseEntity.ok(new JwtToken(token));
        }
        return ResponseEntity.badRequest().build();
    }
    
    private static class JwtToken {
        private String token;

        public JwtToken(String token) {
            super();
            this.token = token;
        }

        @SuppressWarnings("unused")
        public String getToken() {
            return token;
        }

        @SuppressWarnings("unused")
        public void setToken(String token) {
            this.token = token;
        }
    }
}
