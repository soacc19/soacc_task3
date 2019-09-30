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
    public Response getRooms(@Context UriInfo uriInfo) {
        List<Room> rooms = roomService.getRooms();
        
        for (Room room : rooms) {
            // HATEOAS
            if (room.getLinks().isEmpty()) {
                String uri = uriInfo.getBaseUriBuilder()
                        .path(RoomResource.class)
                        .path(Long.toString(room.getId()))
                        .build().toString();
                room.addLink(uri, "self");
                
                uri = uriInfo.getBaseUriBuilder()
                        .path(RoomResource.class)
                        .path(RoomResource.class, "getReservations")
                        .resolveTemplate("roomId", room.getId())
                        .build()
                        .toString();
                room.addLink(uri, "reservations");
            }
        }
        
        return Response.status(Status.OK)
                .entity(rooms)
                .build();
    }
    
    
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") long id, @Context UriInfo uriInfo) throws DataNotFoundException {
        Room resultRoom = roomService.getRoom(id);
        
        // HATEOAS
        if (resultRoom.getLinks().isEmpty()) {
            String uri = uriInfo.getBaseUriBuilder()
                    .path(RoomResource.class)
                    .path(Long.toString(resultRoom.getId()))
                    .build().toString();
            resultRoom.addLink(uri, "self");
            
            uri = uriInfo.getBaseUriBuilder()
                    .path(RoomResource.class)
                    .path(RoomResource.class, "getReservations")
                    .resolveTemplate("roomId", resultRoom.getId())
                    .build()
                    .toString();
            resultRoom.addLink(uri, "reservations");
        }

       
        return Response.status(Status.OK)
                .entity(resultRoom)
                .build();
        
    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRoom(Room newRoom, @Context UriInfo uriInfo) {
        Room addedRoom = roomService.addRoom(newRoom);
        
        // HATEOAS
        if (addedRoom.getLinks().isEmpty()) {
            String uri = uriInfo.getBaseUriBuilder()
                    .path(RoomResource.class)
                    .path(Long.toString(newRoom.getId()))
                    .build().toString();
            addedRoom.addLink(uri, "self");
            
            uri = uriInfo.getBaseUriBuilder()
                    .path(RoomResource.class)
                    .path(RoomResource.class, "getReservations")
                    .resolveTemplate("roomId", addedRoom.getId())
                    .build()
                    .toString();
            addedRoom.addLink(uri, "reservations");
        }

        
        return Response.status(Status.CREATED)
                .entity(addedRoom)
                .build();
    }
    
    
    @PUT
    @Path("/{roomId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRoom(@PathParam("roomId") long id,  Room updatedRoom, @Context UriInfo uriInfo) throws DataNotFoundException {
       
        updatedRoom.setId(id);
        Room resultingRoom = roomService.updateRoom(updatedRoom);
        
        // HATEOAS
        if (updatedRoom.getLinks().isEmpty()) {
            String uri = uriInfo.getBaseUriBuilder()
                    .path(RoomResource.class)
                    .path(Long.toString(resultingRoom.getId()))
                    .build().toString();
            resultingRoom.addLink(uri, "self");
            
            uri = uriInfo.getBaseUriBuilder()
                    .path(RoomResource.class)
                    .path(RoomResource.class, "getReservations")
                    .resolveTemplate("roomId", resultingRoom.getId())
                    .build()
                    .toString();
            resultingRoom.addLink(uri, "reservations");
        }

        
        return Response.status(Status.OK)
                .entity(resultingRoom)
                .build();   
    }
    
    
    @DELETE
    @Path("/{roomId}")
    public Response removeRoom(@PathParam("roomId") long id) {
        // No HATEOAS since links are going to be invalid
        
        roomService.removeRoom(id);
        return Response.status(Status.OK)
                .build();
    }
    
    // ----------------- RESERVATIONS -----------------
    
    
    @GET
    @Path("/{roomId}/reservations")
    public Response getReservations(@PathParam("roomId") long roomId, @Context UriInfo uriInfo) throws DataNotFoundException {
        List<Reservation> reservations =  roomService.getReservations(roomId);
        
        for (Reservation reservation : reservations) {
            // HATEOAS
            if (reservation.getLinks().isEmpty()) {
                String uri = uriInfo.getBaseUriBuilder()
                        .path(RoomResource.class)
                        .path(RoomResource.class, "getReservations")
                        .path(Long.toString(reservation.getId()))
                        .build()
                        .toString();
                reservation.addLink(uri, "self");
            }

        }
        return Response.status(Status.OK)
                .entity(reservations)
                .build();
        
    }
    
    
    @POST
    @Path("/{roomId}/reservations")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReservation(@PathParam("roomId") long roomId, Reservation newReservation, @Context UriInfo uriInfo) {
        /*
         * TODO missing ReservationService for proper HATEOAS
        Reservation newReservation = roomService.addReservation(roomId, newReservation);
        
        // HATEOAS
        String uri = uriInfo.getBaseUriBuilder()
                .path(RoomResource.class)
                .path(RoomResource.class, "getReservations")
                .path(Long.toString(newReservation.getId()))
                .build()
                .toString();
        newReservation.addLink(uri, "self");
        // add newReservation to .entity param
        */
        
        return Response.status(Status.CREATED)
                .entity(roomService.addReservation(roomId, newReservation))
                .build();   
    }
    
    
    @PUT
    @Path("/{roomId}/reservations/{reservationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateReservation(@PathParam("roomId") long roomId, @PathParam("reservationId") long reservationId,
            Reservation updatedReservation, @Context UriInfo uriInfo) throws DataNotFoundException {
        
        /*
         * TODO missing ReservationService for proper HATEOAS
        Reservation resultReservation = roomService.updateReservation(roomId, reservationId, updatedReservation);
        
        // HATEOAS
        String uri = uriInfo.getBaseUriBuilder()
                .path(RoomResource.class)
                .path(RoomResource.class, "getReservations")
                .path(Long.toString(resultReservation.getId()))
                .build()
                .toString();
        resultReservation.addLink(uri, "self");
        // add resultReservation to .entity param
        */
        
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
