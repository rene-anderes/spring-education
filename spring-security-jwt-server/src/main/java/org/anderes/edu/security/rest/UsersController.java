package org.anderes.edu.security.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "users", produces = { APPLICATION_JSON_UTF8_VALUE } )
public class UsersController {

    @Inject
    private UserDetailsManager userManager;
    
    @GetMapping(value = "{username}")
    public ResponseEntity<String> getUser(@PathVariable("username") String username) {
        
        final UserDetails userDetails = userManager.loadUserByUsername(username);
        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        userDetails.getAuthorities().forEach(a -> arrayBuilder.add(a.getAuthority()));
        final JsonArray roles = arrayBuilder.build();
        final JsonObject user = Json.createObjectBuilder().add("username", userDetails.getUsername()).add("roles", roles).build();
   
        return ResponseEntity.ok(user.toString());
    }
}
