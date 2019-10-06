package SisuBeta.SisuRS.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import SisuBeta.SisuRS.classes.Course;
import SisuBeta.SisuRS.services.CourseService;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;

/**
 * Root resource
 */
@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class CourseResource {

    // Service object to handle business logic.
    CourseService courseService = new CourseService();
    
    @GET
    public Response getCourses(@QueryParam("year") int year, @QueryParam("period") int period, @Context UriInfo uriInfo) {
        courseService.fillData();
        List<Course> courses = null;
        if (year > 0 || period > 0 ) { courses = courseService.getCoursesFiltered(year, period); }
        else {
            courses = courseService.getCourses();
        }
        
        for (Course course : courses) {
            // HATEOAS
            if (course.getLinks().isEmpty()) {
                String uri = uriInfo.getBaseUriBuilder()
                        .path(CourseResource.class)
                        .path(Long.toString(course.getId()))
                        .build().toString();
                course.addLink(uri, "self");
                
                uri = uriInfo.getBaseUriBuilder()
                        .path(CourseResource.class)
                        .path(CourseResource.class, "getTeachers")
                        .resolveTemplate("courseId", course.getId())
                        .build()
                        .toString();
                course.addLink(uri, "teachers");
                
                uri = uriInfo.getBaseUriBuilder()
                        .path(CourseResource.class)
                        .path(CourseResource.class, "getStudents")
                        .resolveTemplate("courseId", course.getId())
                        .build()
                        .toString();
                course.addLink(uri, "students");
            }
        }
        

        
        return Response.status(Status.OK)
                .entity(courses)
                .build();
    }
    
    
    @GET
    @Path("/{courseId}")
    public Response getCourse(@PathParam("courseId") long id) throws DataNotFoundException {
        courseService.fillData();
        return Response.status(Status.OK)
                .entity(courseService.getCourse(id))
                .build();
    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCourse(Course newCourse, @Context UriInfo uriInfo) {
        courseService.fillData();
        Course addedCourse = courseService.addCourse(newCourse);
        
        // HATEOAS
        String uri = uriInfo.getBaseUriBuilder()
                .path(CourseResource.class)
                .path(Long.toString(addedCourse.getId()))
                .build().toString();
        addedCourse.addLink(uri, "self");
        
        uri = uriInfo.getBaseUriBuilder()
                .path(CourseResource.class)
                .path(CourseResource.class, "getTeachers")
                .resolveTemplate("courseId", addedCourse.getId())
                .build()
                .toString();
        addedCourse.addLink(uri, "teachers");
        
        uri = uriInfo.getBaseUriBuilder()
                .path(CourseResource.class)
                .path(CourseResource.class, "getStudents")
                .resolveTemplate("courseId", addedCourse.getId())
                .build()
                .toString();
        addedCourse.addLink(uri, "students");
        
        return Response.status(Status.CREATED)
                .entity(addedCourse)
                .build();   
    }
    
    
    @PUT
    @Path("/{courseId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCourse(@PathParam("courseId") long id,  Course newCourse, @Context UriInfo uriInfo) throws DataNotFoundException{
        courseService.fillData();
        Course updatedCourse = courseService.updateCourse(id, newCourse);
        
        // HATEOAS
        String uri = uriInfo.getBaseUriBuilder()
                .path(CourseResource.class)
                .path(Long.toString(updatedCourse.getId()))
                .build().toString();
        updatedCourse.addLink(uri, "self");
        
        uri = uriInfo.getBaseUriBuilder()
                .path(CourseResource.class)
                .path(CourseResource.class, "getTeachers")
                .resolveTemplate("courseId", updatedCourse.getId())
                .build()
                .toString();
        updatedCourse.addLink(uri, "teachers");
        
        uri = uriInfo.getBaseUriBuilder()
                .path(CourseResource.class)
                .path(CourseResource.class, "getStudents")
                .resolveTemplate("courseId", updatedCourse.getId())
                .build()
                .toString();
        updatedCourse.addLink(uri, "students");
        
        return Response.status(Status.OK)
                .entity(updatedCourse)
                .build();
    }
    
    
    @DELETE
    @Path("/{courseId}")
    public Response removeCourse(@PathParam("courseId") long id) {
        courseService.fillData();
        courseService.removeCourse(id);
        return Response.status(Status.OK)
                .build();
    }
    
    // Teachers
    
    @GET
    @Path("/{courseId}/teachers")
    public Response getTeachers(@PathParam("courseId") long courseId) throws DataNotFoundException {
        return Response.status(Status.OK)
                .entity(courseService.getTeachers(courseId))
                .build();
    }
    
    @GET
    @Path("/{courseId}/teachers/{teacherId}")
    public Response getTeacher(@PathParam("courseId") long courseId, @PathParam("teacherId") long teacherId) throws DataNotFoundException {
        return Response.status(Status.OK)
                .entity(courseService.getTeacher(courseId, teacherId))
                .build();
    }
    

    @POST
    @Path("/{courseId}/teachers")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTeacher(@PathParam("courseId") long courseId, long teacherId) {
        return Response.status(Status.CREATED)
                .entity(courseService.addTeacher(courseId, teacherId))
                .build();
    }
    
    
    /* Update is unnecessary when course only stores person's id.
    @PUT
    @Path("/{courseId}/teacher/{teacherId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTeacher(@PathParam("courseId") long courseId, @PathParam("courseId") long teacherId, Person person) {
        return Response.status(Status.CREATED)
                .entity(courseService.updateTeacher(courseId, teacherId, person))
                .build();
    }
    */
    
    @DELETE
    @Path("/{courseId}/teachers/{teacherId}")
    public Response removeTeacher(@PathParam("courseId") long courseId, @PathParam("teacherId") long teacherId) {
    	courseService.removeTeacher(courseId, teacherId);
        return Response.status(Status.OK)
                .build();
    }
    
    // Students
    
    
    @GET
    @Path("/{courseId}/students")
    public Response getStudents(@PathParam("courseId") long courseId) throws DataNotFoundException {
        return Response.status(Status.OK)
                .entity(courseService.getStudents(courseId))
                .build();
    }
    
    @GET
    @Path("/{courseId}/students/{studentId}")
    public Response getStudent(@PathParam("courseId") long courseId, @PathParam("studentId") long studentId) throws DataNotFoundException {
        return Response.status(Status.OK)
                .entity(courseService.getStudent(courseId, studentId))
                .build();
    }
    

    @POST
    @Path("/{courseId}/students")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(@PathParam("courseId") long courseId, long studentId) {
        return Response.status(Status.CREATED)
                .entity(courseService.addStudent(courseId, studentId))
                .build();
    }
    
    
    @DELETE
    @Path("/{courseId}/students/{studentId}")
    public Response removeStudent(@PathParam("courseId") long courseId, @PathParam("studentId") long studentId) {
    	courseService.removeStudent(courseId, studentId);
        return Response.status(Status.OK)
                .build();
    }
}
