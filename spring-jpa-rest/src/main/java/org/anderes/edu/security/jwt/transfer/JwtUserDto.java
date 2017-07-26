package org.anderes.edu.security.jwt.transfer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class JwtUserDto {

    private String username;

    private Collection<String> roles = new HashSet<>();

    public JwtUserDto(Long id, String username, Collection<String> roles) {
        super();
        this.username = username;
        setRoles(roles);
    }
    
    public JwtUserDto(String username, String... roles) {
        super();
        this.username = username;
        setRoles(roles);
    }

    public JwtUserDto() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public JwtUserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public Collection<String> getRoles() {
        return roles;
    }
    
    public String getRolesAsCommaSeparatedString() {
        return roles.stream().collect(Collectors.joining(","));
    }

    public JwtUserDto setRoles(String... roles) {
        this.roles = Arrays.asList(roles);
        return this;
    }
    
    public JwtUserDto setRoles(Collection<String> roles) {
        this.roles = new HashSet<>(roles);
        return this;
    }
}