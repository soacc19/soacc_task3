 package SisuBeta.SisuRS.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import SisuBeta.SisuRS.classes.Course;
import SisuBeta.SisuRS.exceptions.BadInputException;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;

public class CourseService {

    private ArrayList<Course> courses = new ArrayList<Course>();
    private int nextId = 1;
    
    
    /**
     * Default constructor.
     */
    public CourseService() {
    	// DEBUG: test courses.
    	Course c1 = addCourse("Testi1", "T100", "", 2019, 1, 200);
    	Course c2 = addCourse("Testi2", "T200", "", 2020, 2, 200);
    	Course c3 = addCourse("Testi3", "T300", "", 2020, 1, 200);
    	Course c4 = addCourse("Testi4", "T400", "", 2020, 1, 50);
    	c1.addTeacher(100);
    	c1.addStudent(300);
    	c1.addStudent(400);
    }
    
    
    /**
     * Returns all courses.
     * @return
     */
    public ArrayList<Course> getCourses() {
        return this.courses;
    }
    
    
    /**
     * Returns the courses with matching year and period
     * @param year
     * @param period
     * @return course list
     */
    public List<Course> getCoursesFiltered(int year, int period) {
        
        return this.courses.stream()
            .filter( x -> year > 0 ? x.getYear() == year : true)
            .filter( x -> period > 0 ? x.getPeriod() == period : true)
            .collect(Collectors.toList()); 
    }
    
    
    /**
     * Returns the course with the specified ID.
     * @param id
     * @return
     * @throws DataNotFoundException
     */
    public Course getCourse(long id) throws DataNotFoundException {
        for (Course course : this.courses) {
            if (course.getId() == id) {
                return course;
            }
        }
        
        throw new DataNotFoundException("Course with id = " + Long.toString(id) +  " not found.");
    }
  

    /**
     * Returns the index of the course with the specified ID.
     * @param id
     * @return
     * @throws DataNotFoundException
     */
    public int getCourseIndex(long id) throws DataNotFoundException {
    	for (int i = 0; i < this.courses.size(); i++) {
            if (courses.get(i).getId() == id) {
                return i;
            }
        }
        
        throw new DataNotFoundException("Course with id = " + Long.toString(id) +  " not found.");
    }
    
    
    /**
     * Adds a new course using parameters.
     * @param name
     * @param code
     * @param description
     * @param year
     * @param period
     * @param capacity
     * @return The added course object.
     */
    public Course addCourse(String name, String code, String description, int year, int period, int capacity) {
    	return addCourse(new Course(-1, name, code, description, year, period, capacity));
    }


    /**
     * Adds a new course using a course object.
     * @param newCourse
     * @return The added course object.
     * @throws BadInputException
     */
    public Course addCourse(Course newCourse) throws BadInputException {
        // Validate.
    	if (!newCourse.isValid()) {
    		throw new BadInputException("Course is invalid!","course","");
    	}
    	
    	// Add.
        long newId = this.nextId++;
        newCourse.setId(newId);
        this.courses.add(newCourse);
        return newCourse;
    }
    
    
    /**
     * Removes any courses with the specified ID.
     * @param id
     * @throws DataNotFoundException
     */
    public void removeCourse(long id) throws DataNotFoundException {
       if (!this.courses.removeIf(x -> x.getId() == id)) {
    	   throw new DataNotFoundException("Course with id = " + Long.toString(id) +  " not found.");
       }
    }
    
      
    /**
     * Updates a course with the specified ID using a new course object.
     * @param id
     * @param newCourse
     * @return The updated course.
     * @throws BadInputException
     */
    public Course updateCourse(long id, Course newCourse) throws BadInputException {
    	// Validate.
    	if (!newCourse.isValid()) {
    		throw new BadInputException("Course is invalid!","course","");
    	}
        
    	// Update.
        int index = getCourseIndex(id);
        this.courses.set(index, newCourse);
        return newCourse;
    }
    
    
    /**
     * Updates a course with a new course object.
     * @param newCourse
     * @return The updated course.
     * @throws BadInputException
     */
    public Course updateCourse(Course newCourse) throws BadInputException {
    	// Validate.
    	if (newCourse.getId() == -1) {
    		throw new BadInputException("Course ID is invalid!","course","");
    	}
    	// Update.
    	return updateCourse(newCourse.getId(), newCourse);
    }
    
    
    // ----------------- TEACHERS -----------------
    
    
    /**
     * TODO: This should probably return the actual people, not just their ID. 
     * Returns the teachers of the specified course.
     * @param courseId
     * @return
     */
    public ArrayList<Long> getTeachers(long courseId) {
    	for (Course course : courses) {
    		if (course.getId() == courseId) {
    			return course.getTeachers();
    		}
    	}
    	throw new BadInputException("The specified course does not exist.","course id", Long.toString(courseId));
    }
    
    
    /**
     * Adds a teacher's id to the specified course.
     * @param courseId
     * @param teacherId
     * @return true/false success
     * @throws BadInputException
     */
    public boolean addTeacher(long courseId, long teacherId) throws BadInputException {
    	for (Course course : courses) {
    		if (course.getId() == courseId) {
    			return course.addTeacher(teacherId);
    		}
    	}
    	throw new BadInputException("Can't add teacher - the specified course does not exist.","course id", Long.toString(courseId));
    }
    
    
    /**
     * Removes a teacher from the specified course using person id.
     * @param courseId
     * @param teacherId
     */
    public void removeTeacher(long courseId, long teacherId) {
    	for (Course course : courses) {
    		if (course.getId() == courseId) {
    			course.removeTeacher(teacherId);
    		}
    	}
    	throw new BadInputException("Can't remove teacher - the specified course does not exist.","course id", Long.toString(courseId));
    }
    
    
	// ----------------- STUDENTS -----------------
    
    
    /**
     * TODO: This should probably return the actual people, not just their ID.
     * Gets all the students on the course.
     * @param courseId
     * @return
     */
    public ArrayList<Long> getStudents(long courseId) {
    	for (Course course : courses) {
    		if (course.getId() == courseId) {
    			return course.getStudents();
    		}
    	}
    	throw new BadInputException("The specified course does not exist.","course id", Long.toString(courseId));
    }
    
    
    /**
     * Adds a student to the course using the student's personal ID.
     * @param courseId
     * @param studentId
     * @return
     * @throws BadInputException
     */
    public boolean addStudent(long courseId, long studentId) throws BadInputException {
    	for (Course course : courses) {
    		if (course.getId() == courseId) {
    			return course.addStudent(studentId);
    		}
    	}
    	throw new BadInputException("Can't add student - the specified course does not exist.","course id", Long.toString(courseId));
    }
    
    
    /**
     * Removes a student from a course using the student's personal ID.
     * @param courseId
     * @param studentId
     * @return
     */
    public boolean removeStudent(long courseId, long studentId) {
    	for (Course course : courses) {
    		if (course.getId() == courseId) {
    			course.removeStudent(studentId);
    		}
    	}
    	throw new BadInputException("Can't remove student - the specified course does not exist.","course id", Long.toString(courseId));
    }
}
