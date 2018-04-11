package org.anderes.tech.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

import org.anderes.tech.domain.DatabaseInfo;
import org.anderes.tech.domain.DatabaseTechService;
import org.springframework.core.SpringVersion;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tech")
public class TechInfoController {
    
    @Inject 
    private DatabaseTechService service;
    
    @GetMapping(path="info/database", produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<DatabaseInfo> getDatabaseInfo() throws Exception {
        final DatabaseInfo dbName = service.getDatabasename();
        return ResponseEntity.ok(dbName);
    }
    
    @GetMapping(path="info/spring", produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<String> getSpringInfo() throws Exception {
        final JsonObject jsonObject = Json.createObjectBuilder().add("Spring-Version", SpringVersion.getVersion()).build();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(jsonObject.toString());
    }
}
