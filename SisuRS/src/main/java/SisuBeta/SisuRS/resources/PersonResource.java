package SisuBeta.SisuRS.resources;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path; 
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import SisuBeta.SisuRS.classes.Person;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;
import SisuBeta.SisuRS.services.PersonService;
import SisuBeta.SisuRS.db.DbHandler;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class PersonResource {
    PersonService personService = new PersonService();
    DbHandler dbHandler = new DbHandler();
    
    @GET
    public Response getPerson() {
        List<Person> persons = personService.getPersons();
        
        return Response.status(Status.OK)
                .entity(persons)
                .build();
    } 

    @GET
    @Path("/{personId}")
    public Response getPerson(@PathParam("personId")long id) throws DataNotFoundException {
        Person person = personService.getPerson(id);
          
        return Response.status(Status.OK)
                .entity(person)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(Person newPerson, @Context UriInfo uriInfo) {
        Person addedPerson = personService.addPerson(newPerson);
        
        // HATEOAS
        String uri = uriInfo.getBaseUriBuilder()
                .path(PersonResource.class)
                .path(Long.toString(newPerson.getId()))
                .build().toString();
        
        addedPerson.addLink(uri, "self");
        
        return Response.status(Status.CREATED)
                .entity(addedPerson)
                .build();
    }
    
    @PUT
    @Path("/{personId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson(@PathParam("personId") long id,  Person updatedPerson) throws DataNotFoundException {
        updatedPerson.setId(id);
        Person resultingPerson = personService.updatePerson(updatedPerson);
        
        return Response.status(Status.OK)
                .entity(resultingPerson)
                .build();
    }

    
    @DELETE
    @Path("/{personId}")
    public Response removePerson(@PathParam("personId") long id) {
        personService.removePerson(id);
        return Response.status(Status.OK)
                .build();
    }
}
