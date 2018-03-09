package org.anderes.edu.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Siehe dazu:
 * <a href="https://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html#abstractsecuritywebapplicationinitializer-without-existing-spring">AbstractSecurityWebApplicationInitializer without Existing Spring</a>
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    
    private SecurityWebApplicationInitializer() {
        super(WebSecurityConfig.class);
    }

    public static void init() {
        new SecurityWebApplicationInitializer();
    }

}
