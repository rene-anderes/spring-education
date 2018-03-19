package org.anderes.web.security;

import java.util.Base64;

import javax.inject.Inject;

import org.anderes.web.configuration.AppConfig;
import org.anderes.web.configuration.WebMvcConfig;
import org.anderes.web.configuration.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebSecurityConfig.class, WebMvcConfig.class })
@WebAppConfiguration
public class EncodeThePassword {

    @Value("${jwt.secret}")
    private String secret;
    @Inject
    private PasswordEncoder passwordEncoder;
    
    @Test
    public void getEncodePassword() {
     
        System.out.println("------- PasswordEncoder --------->: " + passwordEncoder.encode("password"));
        
    }
    
    @Test
    public void secretBase64Encoded() {
        
        System.out.printf("------- Base64 (%s) --------->: %s%n", secret, Base64.getEncoder().encodeToString(secret.getBytes()));
        
    }
}
