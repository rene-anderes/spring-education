package org.anderes.edu.security.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;

import org.anderes.edu.security.domain.UserData;
import org.anderes.edu.security.jwt.transfer.JwtUserDto;
import org.anderes.edu.security.jwt.util.JwtTokenGenerator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class LoginJWTController {

    @Inject 
    private UserRepository repository;
    @Inject
    private JwtTokenGenerator jwtTokenGenerator;
      
    @RequestMapping(value="login", method = POST, consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = { APPLICATION_JSON_UTF8_VALUE })
    public HttpEntity<JwtToken> login(@RequestBody UserData userData) {
        
        Optional<Long> optionalId = repository.checkLogin(userData.getName(), userData.getPassword());
        if (optionalId.isPresent()) {
            Collection<String> roles = repository.getRolesByName(userData.getName());
            JwtUserDto user = new JwtUserDto(optionalId.get(), userData.getName(), roles);
            final String token = jwtTokenGenerator.generateToken(user);
            return ResponseEntity.ok(new JwtToken(token));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
