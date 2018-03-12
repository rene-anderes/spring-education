package org.anderes.edu.security.jwt.rest;

import java.time.LocalDateTime;
import java.util.Set;

public interface TokenGenerator {

    String createToken(final String username, final String... roles);
    
    String createToken(final String username, final Set<String> roles);
    
    TokenGenerator setExpiration(final LocalDateTime expirationDate);
}
