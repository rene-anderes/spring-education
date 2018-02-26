package org.anderes.edu.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    
    private SecurityWebApplicationInitializer() {
        super(WebSecurityConfig.class);
    }

    public static void init() {
        new SecurityWebApplicationInitializer();
    }

}
