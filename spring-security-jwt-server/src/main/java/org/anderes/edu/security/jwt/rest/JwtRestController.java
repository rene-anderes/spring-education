package org.anderes.edu.security.jwt.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.MediaType.*;

@Path("token")
public class JwtRestController {

    @POST
    @Produces(value = { APPLICATION_JSON } )
    public Response getJwtToken() {
        
        return Response.ok().build();
    }
}
