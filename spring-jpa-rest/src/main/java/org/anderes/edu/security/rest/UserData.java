package org.anderes.edu.security.rest;

import java.util.HashSet;
import java.util.Set;

public class UserData {
    
    private Long userId;
    private String name;
    private String password;
    private Set<String> roles = new HashSet<>();
   
    UserData() {
        super();
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}
