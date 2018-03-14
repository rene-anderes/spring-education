package org.anderes.edu.security.rest;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public interface TokenGenerator {

    String createToken(@NotBlank String username, @NotNull String... roles);
    
    String createToken(@NotBlank String username, @NotNull Set<String> roles);
    
    TokenGenerator setExpiration(@NotNull LocalDateTime expirationDate);
}
