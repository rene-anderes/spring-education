package org.anderes.web.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUser implements UserDetails {

    private static final long serialVersionUID = 1L;
    private final String username;
    private final String token;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(String username, String token, Collection<String> roles) {
        Validate.notBlank(username);
        Validate.notBlank(token);
        Validate.notNull(roles);
        this.username = username;
        this.token = token;
        this.authorities = roles.stream()
                        .map(r -> new SimpleGrantedAuthority(r))
                        .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

}
