package org.anderes.edu.security.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "users", produces = { APPLICATION_JSON_UTF8_VALUE } )
public class UsersController {

    @GetMapping(value = "{username}")
    public ResponseEntity<String> getUser(@PathVariable("username") String username) {
        
        final JsonArray roles = Json.createArrayBuilder().add("USER").add("ANONYMOUS").build();
        final JsonObject user = Json.createObjectBuilder().add("username", username).add("roles", roles).build();
   
        return ResponseEntity.ok(user.toString());
    }
}
