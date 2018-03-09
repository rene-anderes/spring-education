package org.anderes.edu.security.jwt.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

public class JwtRestControllerJerseyTest extends JerseyTest {
    
    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(JwtRestController.class);
    }

    @Test
    public void shouldBeGetToken() {
        
        final Response hello = target("token").request().post(null);
        
        assertThat(hello.getStatus(), is(200));
        assertEquals("", hello.readEntity(String.class));
    }
}
