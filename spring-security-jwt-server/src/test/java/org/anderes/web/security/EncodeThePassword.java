package org.anderes.web.security;

import javax.inject.Inject;

import org.anderes.web.configuration.AppConfig;
import org.anderes.web.configuration.WebMvcConfig;
import org.anderes.web.configuration.WebSecurityConfig;
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
