package SisuBeta.SisuRS.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import SisuBeta.SisuRS.classes.User;
import SisuBeta.SisuRS.db.DbHandler;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;
import SisuBeta.SisuRS.services.UserService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@RolesAllowed("admin")
public class UserResource {
    UserService userService = new UserService();
    DbHandler dbHandler = new DbHandler();
    
    @GET 
    public Response getUsers(@Context UriInfo uriInfo) {
        userService.fillData();
        List<User> users = userService.getUsers();
        
        for (User user : users) {
            // HATEOAS
            if (user.getLinks().isEmpty()) {
                String uri = uriInfo.getBaseUriBuilder()
                        .path(UserResource.class)
                        .path(Long.toString(user.getId()))
                        .build().toString();
                user.addLink(uri, "self");
            }
        }
        
        return Response.status(Status.OK)
                .entity(users)
                .build();
    }
    
    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") long id, @Context UriInfo uriInfo) throws DataNotFoundException {
        userService.fillData();
        User user = userService.getUser(id);
        
        if (user.getLinks().isEmpty()) {
            String uri = uriInfo.getBaseUriBuilder()
                    .path(UserResource.class)
                    .path(Long.toString(user.getId()))
                    .build().toString();
            user.addLink(uri, "self");
        }
        
        return Response.status(Status.OK)
                .entity(user)
                .build();
    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User newUser, @Context UriInfo uriInfo) {
        userService.fillData();
        User addedUser = userService.addUser(newUser);
        
        // HATEOAS
        String uri = uriInfo.getBaseUriBuilder()
                .path(UserResource.class)
                .path(Long.toString(addedUser.getId()))
                .build().toString();
        addedUser.addLink(uri, "self");
        
        return Response.status(Status.CREATED)
                .entity(addedUser)
                .build();   
    }
    
    
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userId") long id,  User newUser, @Context UriInfo uriInfo) throws DataNotFoundException{
        userService.fillData();
        User updatedUser = userService.updateUser(id, newUser);
        
        // HATEOAS
        String uri = uriInfo.getBaseUriBuilder()
                .path(UserResource.class)
                .path(Long.toString(updatedUser.getId()))
                .build().toString();
        updatedUser.addLink(uri, "self");
        
        return Response.status(Status.OK)
                .entity(updatedUser)
                .build();
    }
    
    
    @DELETE
    @Path("/{userId}")
    public Response removeUser(@PathParam("userId") long id) {
        userService.fillData();
        userService.removeUser(id);
        return Response.status(Status.OK)
                .build();
    }

}
