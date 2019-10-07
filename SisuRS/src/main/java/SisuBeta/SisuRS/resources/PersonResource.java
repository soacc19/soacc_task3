package SisuBeta.SisuRS.resources;

import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
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

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@DenyAll
public class PersonResource {
    PersonService personService = new PersonService();
    
    @GET
    @RolesAllowed("admin")
    public Response getPerson(@Context UriInfo uriInfo) {
        personService.fillData();
        List<Person> persons = personService.getPersons();
        
        for (Person person : persons) {
            // HATEOAS
            if (person.getLinks().isEmpty()) {
                String uri = uriInfo.getBaseUriBuilder()
                        .path(CourseResource.class)
                        .path(Long.toString(person.getId()))
                        .build().toString();
                person.addLink(uri, "self");
            }
        }
        
        return Response.status(Status.OK)
                .entity(persons)
                .build();
    } 

    @GET
    @Path("/{personId}")
    @RolesAllowed("admin")
    public Response getPerson(@PathParam("personId")long id, @Context UriInfo uriInfo) throws DataNotFoundException {
        personService.fillData();
        Person person = personService.getPerson(id);
        
        // HATEOAS
        if (person.getLinks().isEmpty()) {
            String uri = uriInfo.getBaseUriBuilder()
                    .path(PersonResource.class)
                    .path(Long.toString(person.getId()))
                    .build().toString();
            
            person.addLink(uri, "self");
        }

          
        return Response.status(Status.OK)
                .entity(person)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response addPerson(Person newPerson, @Context UriInfo uriInfo) {
        personService.fillData();
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
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson(@PathParam("personId") long id,  Person updatedPerson, @Context UriInfo uriInfo) throws DataNotFoundException {
        personService.fillData();
        Person resultingPerson = personService.updatePerson(id, updatedPerson);
        
        // HATEOAS
        String uri = uriInfo.getBaseUriBuilder()
                .path(PersonResource.class)
                .path(Long.toString(updatedPerson.getId()))
                .build().toString();
        
        updatedPerson.addLink(uri, "self");
        
        return Response.status(Status.OK)
                .entity(resultingPerson)
                .build();
    }

    
    @DELETE
    @Path("/{personId}")
    @RolesAllowed("admin")
    public Response removePerson(@PathParam("personId") long id) {
        personService.fillData();
        personService.removePerson(id);
        return Response.status(Status.OK)
                .build();
    }
}
