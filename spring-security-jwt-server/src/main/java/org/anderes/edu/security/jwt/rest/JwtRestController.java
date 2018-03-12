package org.anderes.edu.security.jwt.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users", produces = { APPLICATION_JSON_UTF8_VALUE } )
public class JwtRestController {
    
    @Inject
    private TokenGenerator tokenGenerator;

    @PostMapping(value="token")
    public ResponseEntity<String> getJwtToken() {
        
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() == null) {
            throw new IllegalStateException();
        }
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String username = userDetails.getUsername();
        final Set<String> roles = userDetails.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet());
        final String token = tokenGenerator.createToken(username, roles);
        
        final JsonObject jwt = Json.createObjectBuilder().add("token", token).build();
   
        return ResponseEntity.ok(jwt.toString());
    }
}
