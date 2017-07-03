package org.anderes.edu.security.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserData {
    
    private Long userId;
    private String name;
    private String password;
    private Set<String> roles = new HashSet<>();
   
    UserData() {
        super();
    }
    
    public UserData(Long userId, String name, String password) {
        super();
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public UserData setName(String name) {
        this.name = name;
        return this;
    }
    public String getPassword() {
        return password;
    }
    public UserData setPassword(String password) {
        this.password = password;
        return this;
    }
    
    @JsonIgnore
    public Long getUserId() {
        return userId;
    }

    @JsonIgnore
    public UserData setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    @JsonIgnore
    public UserData setRoles(Collection<String> roles) {
        this.roles = new HashSet<>(roles);
        return this;
    }

    @JsonIgnore
    public UserData setRoles(String... roles) {
        this.roles = new HashSet<>(Arrays.asList(roles));
        return this;
    }

}
