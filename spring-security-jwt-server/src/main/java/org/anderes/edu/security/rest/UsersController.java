package org.anderes.edu.security.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.anderes.edu.security.rest.dto.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "users" )
public class UsersController {

    @Inject
    private UserDetailsManager userManager;
    
    @GetMapping(value = "{username}", produces = { APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<String> getUser(@PathVariable("username") String username) {
        
        UserDetails userDetails;
        try {
            userDetails = userManager.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        userDetails.getAuthorities().forEach(a -> arrayBuilder.add(a.getAuthority()));
        final JsonArray roles = arrayBuilder.build();
        final JsonObject user = Json.createObjectBuilder().add("username", userDetails.getUsername()).add("roles", roles).build();
   
        return ResponseEntity.ok(user.toString());
    }
    
    @PostMapping(consumes = { APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_VALUE })
    public ResponseEntity<?> insertNewUser(final @RequestBody AppUser user) {
        
        if (userManager.userExists(user.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        
        final Collection<? extends GrantedAuthority> authorities = user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(r))
                        .collect(Collectors.toList());
        final UserDetails userDetails = new User(user.getUsername(), user.getPassword(), authorities);
        userManager.createUser(userDetails);
        
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}").buildAndExpand(userDetails.getUsername()).toUri();
        return ResponseEntity.created(location).build();
    }
 
    @DeleteMapping(value = "{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        
        if (!userManager.userExists(username)) {
            return ResponseEntity.notFound().build();
        }
        
        userManager.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}
