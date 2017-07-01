package org.anderes.edu.security.jwt.transfer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class JwtUserDto {

    private Long id;

    private String username;

    private Collection<String> roles = new HashSet<>();

    public JwtUserDto(Long id, String username, Collection<String> roles) {
        super();
        this.id = id;
        this.username = username;
        setRoles(roles);
    }
    
    public JwtUserDto(Long id, String username, String... roles) {
        super();
        this.id = id;
        this.username = username;
        setRoles(roles);
    }

    public JwtUserDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public JwtUserDto setId(Long id) {
        this.id = id;
        return this;
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