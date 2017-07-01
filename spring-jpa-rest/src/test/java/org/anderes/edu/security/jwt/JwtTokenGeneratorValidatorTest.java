package org.anderes.edu.security.jwt;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import javax.inject.Inject;

import org.anderes.edu.security.jwt.transfer.JwtUserDto;
import org.anderes.edu.security.jwt.util.JwtTokenGenerator;
import org.anderes.edu.security.jwt.util.JwtTokenValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
                "classpath:unittest-jwt-application-context.xml"
})
public class JwtTokenGeneratorValidatorTest {
    
    @Inject
    private JwtTokenValidator validator;
    @Inject
    private JwtTokenGenerator jwtTokenGenerator;
    
    @Test
    public void checkToken() {
        // given
        JwtUserDto user = new JwtUserDto(42L, "Anderes", "Admin", "User");
        
        // when
        String token = jwtTokenGenerator.generateToken(user);
        
        // then
        assertThat(token, is(not(nullValue())));
        
        // when
        Optional<JwtUserDto> userFromToken = validator.parseToken(token);
        
        // then
        assertThat(userFromToken, is(not(nullValue())));
        assertThat(userFromToken.isPresent(), is(true));
        assertThat(userFromToken.get().getId(), is(42L));
        assertThat(userFromToken.get().getUsername(), is("Anderes"));
        assertThat(userFromToken.get().getRoles(), hasItems("Admin", "User"));
    }

}
