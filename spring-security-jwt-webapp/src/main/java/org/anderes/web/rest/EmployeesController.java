package org.anderes.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;

import javax.validation.Valid;

import org.anderes.web.rest.dto.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "employees" )
public class EmployeesController {
    
    @GetMapping(value = "{lastname}", produces = { APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<Employee> getUser(@PathVariable("lastname") String lastname) {
        
        final Employee employee = new Employee(lastname, "Bill");
        return ResponseEntity.ok(employee);
    }
    
    @PostMapping(consumes = { APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_VALUE })
    public ResponseEntity<?> insertNewUser(final @Valid @RequestBody Employee employee) {
        
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{lastname}").buildAndExpand(employee.getLastname()).toUri();
        return ResponseEntity.created(location).build();
    }
 
    @DeleteMapping(value = "{lastname}")
    public ResponseEntity<?> deleteUser(@PathVariable("lastname") String lastname) {
       
        return ResponseEntity.ok().build();
    }
    
    @PutMapping(value = "{lastname}", consumes = { APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateUser(@PathVariable("lastname") String lastname, final @Valid @RequestBody Employee user) {
               
        return ResponseEntity.ok().build();
    }
    
}
