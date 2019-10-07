package SisuBeta.SisuRS.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import SisuBeta.SisuRS.classes.Course;
import SisuBeta.SisuRS.classes.Person;
import SisuBeta.SisuRS.services.CourseService;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;

/**
 * Root resource
 */
@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@DenyAll
public class CourseResource {
    // Service object to handle business logic.
    CourseService courseService = new CourseService();
    
    @GET
    @RolesAllowed({"admin", "faculty", "student"})
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
    @RolesAllowed({"admin", "faculty", "student"})
    public Response getCourse(@PathParam("courseId") long id, @Context UriInfo uriInfo) throws DataNotFoundException {
        courseService.fillData();
        Course course = courseService.getCourse(id);
        
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

        return Response.status(Status.OK)
                .entity(course)
                .build();
    }
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
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
    @RolesAllowed("admin")
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
    @RolesAllowed("admin")
    public Response removeCourse(@PathParam("courseId") long id) {
        courseService.fillData();
        courseService.removeCourse(id);
        return Response.status(Status.OK)
                .build();
    }
    
    // Teachers
    
    @GET
    @Path("/{courseId}/teachers")
    @RolesAllowed({"admin", "faculty", "student"})
    public Response getTeachers(@PathParam("courseId") long courseId, @Context UriInfo uriInfo) throws DataNotFoundException {
        courseService.fillData();
        List<Person> teachers = courseService.getTeachers(courseId);
        
        for (Person teacher : teachers) {
            // HATEOAS
            if (teacher.getLinks().isEmpty()) {
                String uri = uriInfo.getBaseUriBuilder()
                        .path(CourseResource.class)
                        .path(Long.toString(teacher.getId()))
                        .build().toString();
                teacher.addLink(uri, "self");
            }
        }
        
        return Response.status(Status.OK)
                .entity(teachers)
                .build();
    }
    
    @GET
    @Path("/{courseId}/teachers/{teacherId}")
    @RolesAllowed({"admin", "faculty", "student"})
    public Response getTeacher(@PathParam("courseId") long courseId, @PathParam("teacherId") long teacherId,
            @Context UriInfo uriInfo) throws DataNotFoundException {
        courseService.fillData();
        Person teacher = courseService.getTeacher(courseId, teacherId);
        
        // HATEOAS
        if (teacher.getLinks().isEmpty()) {
            String uri = uriInfo.getBaseUriBuilder()
                    .path(CourseResource.class)
                    .path(Long.toString(teacher.getId()))
                    .build().toString();
            teacher.addLink(uri, "self");
        }
        
        return Response.status(Status.OK)
                .entity(teacher)
                .build();
    }
    

    @POST
    @Path("/{courseId}/teachers")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response addTeacher(@PathParam("courseId") long courseId, long teacherId, @Context UriInfo uriInfo) {
        courseService.fillData();
        Person addedTeacher = courseService.addTeacher(courseId, teacherId);
        
        // HATEOAS
        String uri = uriInfo.getBaseUriBuilder()
                .path(CourseResource.class)
                .path(Long.toString(addedTeacher.getId()))
                .build().toString();
        addedTeacher.addLink(uri, "self");
        
        return Response.status(Status.CREATED)
                .entity(addedTeacher)
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
    @RolesAllowed("admin")
    public Response removeTeacher(@PathParam("courseId") long courseId, @PathParam("teacherId") long teacherId) {
        courseService.fillData();
        courseService.removeTeacher(courseId, teacherId);
        return Response.status(Status.OK)
                .build();
    }
    
    // Students
    
    
    @GET
    @Path("/{courseId}/students")
    @RolesAllowed({"admin", "faculty"})
    public Response getStudents(@PathParam("courseId") long courseId, @Context UriInfo uriInfo) throws DataNotFoundException {
        courseService.fillData();
        List<Person> students = courseService.getStudents(courseId);
        
        for (Person student : students) {
            // HATEOAS
            if (student.getLinks().isEmpty()) {
                String uri = uriInfo.getBaseUriBuilder()
                        .path(CourseResource.class)
                        .path(Long.toString(student.getId()))
                        .build().toString();
                student.addLink(uri, "self");
            }
        }
        
        return Response.status(Status.OK)
                .entity(students)
                .build();
    }
    
    @GET
    @Path("/{courseId}/students/{studentId}")
    @RolesAllowed({"admin", "faculty"})
    public Response getStudent(@PathParam("courseId") long courseId, @PathParam("studentId") long studentId,
            @Context UriInfo uriInfo) throws DataNotFoundException {
        courseService.fillData();
        Person student = courseService.getStudent(courseId, studentId);
        
        // HATEOAS
        if (student.getLinks().isEmpty()) {
            String uri = uriInfo.getBaseUriBuilder()
                    .path(CourseResource.class)
                    .path(Long.toString(student.getId()))
                    .build().toString();
            student.addLink(uri, "self");
        }
        
        return Response.status(Status.OK)
                .entity(student)
                .build();
    }
    

    @POST
    @Path("/{courseId}/students")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "faculty"})
    public Response addStudent(@PathParam("courseId") long courseId, long studentId, @Context UriInfo uriInfo) {
        courseService.fillData();
        Person addedStudent = courseService.addStudent(courseId, studentId);
        
        // HATEOAS
        String uri = uriInfo.getBaseUriBuilder()
                .path(CourseResource.class)
                .path(Long.toString(addedStudent.getId()))
                .build().toString();
        addedStudent.addLink(uri, "self");
        
        return Response.status(Status.CREATED)
                .entity(addedStudent)
                .build();
    }
    
    
    @DELETE
    @Path("/{courseId}/students/{studentId}")
    @RolesAllowed({"admin", "faculty"})
    public Response removeStudent(@PathParam("courseId") long courseId, @PathParam("studentId") long studentId) {
        courseService.fillData();
        courseService.removeStudent(courseId, studentId);
        return Response.status(Status.OK)
                .build();
    }
}
