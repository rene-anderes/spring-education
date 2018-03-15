package org.anderes.edu.security;

import javax.inject.Inject;

import org.anderes.edu.configuration.AppConfig;
import org.anderes.edu.configuration.WebMvcConfig;
import org.anderes.edu.configuration.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, WebSecurityConfig.class, WebMvcConfig.class })
@WebAppConfiguration
public class EncodeThePassword {

    @Inject
    private PasswordEncoder passwordEncoder;
    
    @Test
    public void getEncodePassword() {
     
        System.out.println("---------------->: " + passwordEncoder.encode("password"));
        
    }
}
