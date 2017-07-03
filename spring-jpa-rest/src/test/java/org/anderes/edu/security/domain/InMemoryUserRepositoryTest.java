package org.anderes.edu.security.domain;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.anderes.edu.security.rest.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
                "classpath:application-context.xml",
                "classpath:unittest-jpa-context.xml"
})
@WebAppConfiguration
public class InMemoryUserRepositoryTest {

    @Inject
    private UserRepository repository;
    
    @Test
    public void checkLogin() {
        
        // when
        final Optional<Long> userId= repository.checkLogin("admin", "password");
        
        // then
        assertThat(userId, is(not(nullValue())));
    }
    
    @Test
    public void checkRolesByUsername() {
        
        // when
        final Set<String> roles = repository.getRolesByName("admin");
        
        // then
        assertThat(roles, hasItems("ROLE_ADMIN", "ROLE_USER"));
    }
}
