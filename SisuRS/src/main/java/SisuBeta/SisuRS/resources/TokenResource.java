package SisuBeta.SisuRS.resources;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import SisuBeta.SisuRS.exceptions.NotImplementedException;
import SisuBeta.SisuRS.services.UserService;
import SisuBeta.SisuRS.token.JWTService;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class TokenResource {
    UserService userService = new UserService();
    
    @GET
    public Response getAuthTokens(@Context ContainerRequestContext crc) {
        System.out.println("DEBUG crc: " + crc.getProperty("username"));
        
        String token = JWTService.buildToken(crc.getProperty("username").toString());
        
        return Response.status(Status.OK)
                .entity("{ \"token\": \"" + token + "\"}")
                .build();
    }
    

    @GET
    @Path("/{tokenId}")
    public Response getAuthToken(@PathParam("tokenId") long id) throws NotImplementedException {
     // TODO not implemented
        throw new NotImplementedException("Method GET on resource '/token/{tokenId}' not implemented!");
    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAuthToken() throws NotImplementedException {
        // TODO not implemented
        throw new NotImplementedException("Method POST on resource '/token' not implemented!");
    }
    
    
    @PUT
    @Path("/{tokenId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAuthToken(@PathParam("tokenId") long id) throws NotImplementedException {
        // TODO not implemented
        throw new NotImplementedException("Method PUT on resource '/token/{tokenId}' not implemented!");
    }
    
    @DELETE
    @Path("/{tokenId}")
    public Response removeAuthToken(@PathParam("tokenId") long id) {
        // TODO not implemented
        throw new NotImplementedException("Method DELETE on resource '/token/{tokenId}' not implemented!");
    }

}
