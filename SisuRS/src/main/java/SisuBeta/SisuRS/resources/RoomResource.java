package SisuBeta.SisuRS.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import SisuBeta.SisuRS.classes.Room;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;
import SisuBeta.SisuRS.services.RoomService;

/**
 * Root resource
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class RoomResource {

    // Service for handling the heavy lifting
    RoomService roomService = new RoomService();
    
    //TODO: Reservations-nested resource delegation!
    
    @GET
    public Response getRooms() {
        List<Room> rooms = roomService.getRooms();
        return Response.status(Status.OK)
                .entity(rooms)
                .build();
    }
    
    
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") long id) throws DataNotFoundException {
        
        Room resultRoom = roomService.getRoom(id);
       
        return Response.status(Status.OK)
                .entity(resultRoom)
                .build();
        
    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRoom(Room newRoom) {
        
       Room addedRoom = roomService.addRoom(newRoom);
        
        return Response.status(Status.CREATED)
                .entity(addedRoom)
                .build();   
    }
    
    
    @PUT
    @Path("/{roomId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRoom(@PathParam("roomId") long id,  Room updatedRoom) throws DataNotFoundException{
       
       updatedRoom.setId(id);
       Room resultingRoom = roomService.updateRoom(updatedRoom);
        
        return Response.status(Status.OK)
                .entity(resultingRoom)
                .build();   
    }
    
    
    @DELETE
    @Path("/{roomId}")
    public Response removeRoom(@PathParam("roomId") long id) {
        
        roomService.removeRoom(id);
        return Response.status(Status.OK)
                .build();
    }
    
    
}
