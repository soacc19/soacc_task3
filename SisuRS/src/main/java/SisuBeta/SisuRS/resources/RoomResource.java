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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import SisuBeta.SisuRS.classes.Reservation;
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
    public Response addRoom(Room newRoom, @Context UriInfo uriInfo) {
        Room addedRoom = roomService.addRoom(newRoom);
        // TODO: should it stay in Resource or moved to Service?
        // HATEOAS
        String uri = uriInfo.getBaseUriBuilder()
                .path(RoomResource.class)
                .path(Long.toString(newRoom.getId()))
                .build().toString();
        
        addedRoom.addLink(uri, "self");
        
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
    
    // ----------------- RESERVATIONS -----------------
    
    
    @GET
    @Path("/{roomId}/reservations")
    public Response getReservations(@PathParam("roomId") long roomId) throws DataNotFoundException {
        return Response.status(Status.OK)
                .entity(roomService.getReservations(roomId))
                .build();
        
    }
    
    
    @POST
    @Path("/{roomId}/reservations")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReservation(@PathParam("roomId") long roomId, Reservation newReservation) {
        return Response.status(Status.CREATED)
                .entity(roomService.addReservation(roomId, newReservation))
                .build();   
    }
    
    
    @PUT
    @Path("/{roomId}/reservations/{reservationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateReservation(@PathParam("roomId") long roomId, @PathParam("reservationId") long reservationId, Reservation updatedReservation) throws DataNotFoundException {
        updatedReservation.setId(reservationId);
        return Response.status(Status.OK)
                .entity(roomService.updateReservation(roomId, reservationId, updatedReservation))
                .build();   
    }
    
    
    @DELETE
    @Path("/{roomId}/reservations/{reservationId}")
    public Response removeReservation(@PathParam("roomId") long roomId, @PathParam("reservationId") long reservationId) {
        roomService.removeReservation(roomId, reservationId);
        return Response.status(Status.OK)
                .build();
    }
}
