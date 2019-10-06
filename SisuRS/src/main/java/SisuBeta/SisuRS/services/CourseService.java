 package SisuBeta.SisuRS.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import SisuBeta.SisuRS.classes.Course;
import SisuBeta.SisuRS.classes.Person;
import SisuBeta.SisuRS.classes.PersonRole;
import SisuBeta.SisuRS.db.DbHandler;
import SisuBeta.SisuRS.exceptions.BadInputException;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;
import SisuBeta.SisuRS.other.PersonRoleMapper;

public class CourseService {
    private List<Course> courses = new ArrayList<Course>();
    private List<Person> persons = new ArrayList<Person>();
    private long nextId = 1;
    private DbHandler dbHandler = new DbHandler();
    
    /**
     * Default constructor.
     */
    public CourseService() {
    }
    
    public void fillData() {
        courses = dbHandler.selectAllCourses();
        persons = dbHandler.selectAllPersons();
        
        for (Course course : courses) {
            fillCourseWithStudents(course.getId());
            fillCourseWithTeachers(course.getId());
        }
    }
    
    
    /**
     * Returns all courses.
     * @return
     */
    public List<Course> getCourses() {
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
        // Validate
        
        // Add to DB
        long highestIdInTable = dbHandler.selectHighestIdFromTable("Course");
        if (this.nextId <= highestIdInTable) {
            this.nextId = highestIdInTable + 1;
        }
        dbHandler.insertOrDeleteCourse("insert", newCourse, this.nextId);
        
        // create and insert students
        if (!newCourse.getStudents().isEmpty()) {
            dbHandler.createOrDropCourseStudentsTeachersTable("create", this.nextId, PersonRoleMapper.personRoleToString(PersonRole.STUDENT));
            for (long studentId : newCourse.getStudents()) {
                dbHandler.insertOrDeleteCourseStudentTeacher("insert", PersonRoleMapper.personRoleToString(PersonRole.STUDENT), this.nextId, studentId, false);
            }
        }
        // create and insert teachers
        if (!newCourse.getTeachers().isEmpty()) {
            dbHandler.createOrDropCourseStudentsTeachersTable("create", this.nextId, PersonRoleMapper.personRoleToString(PersonRole.TEACHER));
            for (long teacherId : newCourse.getTeachers()) {
                dbHandler.insertOrDeleteCourseStudentTeacher("insert", PersonRoleMapper.personRoleToString(PersonRole.TEACHER), this.nextId, teacherId, false);
            }
        }
        

        // Add locally
        newCourse.setId(this.nextId++);
        this.courses.add(newCourse);
        
        return newCourse;
    }
    
    /**
     * Removes any courses with the specified ID.
     * @param id
     * @throws DataNotFoundException
     */
    public void removeCourse(long id) throws DataNotFoundException {
        dbHandler.insertOrDeleteCourse("delete", null, id);
        dbHandler.createOrDropCourseStudentsTeachersTable("drop", id, PersonRoleMapper.personRoleToString(PersonRole.STUDENT));
        dbHandler.createOrDropCourseStudentsTeachersTable("drop", id, PersonRoleMapper.personRoleToString(PersonRole.TEACHER));
        
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

        // Update DB
        dbHandler.insertOrDeleteCourse("insert", newCourse, id);
        
        dbHandler.insertOrDeleteCourseStudentTeacher("delete", PersonRoleMapper.personRoleToString(PersonRole.STUDENT), id, 0, true);
        dbHandler.insertOrDeleteCourseStudentTeacher("delete", PersonRoleMapper.personRoleToString(PersonRole.TEACHER), id, 0, true);
        for (long studentId : newCourse.getStudents()) {
            dbHandler.insertOrDeleteCourseStudentTeacher("insert", PersonRoleMapper.personRoleToString(PersonRole.STUDENT), id, studentId, false);
        }
        for (long teacherId : newCourse.getTeachers()) {
            
            dbHandler.insertOrDeleteCourseStudentTeacher("insert", PersonRoleMapper.personRoleToString(PersonRole.TEACHER), id, teacherId, false);
        }
        
        // Update locally
        int index = getCourseIndex(id);
        this.courses.set(index, newCourse);
        this.courses.get(index).setId(id); // just in case if in update not expected ID was provided
        
        return newCourse;
    }
    
//    TODO Since we providing update only by PUT with ID param in URL, this is not useful
//    /**
//     * Updates a course with a new course object.
//     * @param newCourse
//     * @return The updated course.
//     * @throws BadInputException
//     */
//    public Course updateCourse(Course newCourse) throws BadInputException {
//    	// Validate.
//    	if (newCourse.getId() == -1) {
//    		throw new BadInputException("Course ID is invalid!","course","");
//    	}
//    	
//    	// Update.
//    	return updateCourse(newCourse.getId(), newCourse);
//    }
    
    public boolean fillCourseWithStudents(long id) {
        courses.get(getCourseIndex(id)).setStudents(dbHandler.selectAllCourseStudentsTeachers(id,
                PersonRoleMapper.personRoleToString(PersonRole.STUDENT)));
        return true;
    }
    
    public boolean fillCourseWithTeachers(long id) {
        courses.get(getCourseIndex(id)).setTeachers(dbHandler.selectAllCourseStudentsTeachers(id,
                PersonRoleMapper.personRoleToString(PersonRole.TEACHER)));
        return true;
    }
    
    
    // ----------------- TEACHERS -----------------
    
    /**
     * Gets all the teachers of the course.
     * @param courseId
     * @return
     */
    public List<Person> getTeachers(long courseId) {
        List<Person> returner = new ArrayList<Person>();
        List<Long> courseTeachers = courses.get(getCourseIndex(courseId)).getTeachers();
        
        for (Person person : persons) {
            for (Long teacher : courseTeachers) {
                if (teacher == person.getId()) {
                    returner.add(person);
                }
            }
        }
        return returner;
    }
    
    /**
     * Gets specific teacher on the course.
     * @param courseId
     * @param teacherId
     * @return
     */
    public Person getTeacher(long courseId, long teacherId) {
        courses.get(getCourseIndex(courseId)).getTeachers().get(getTeacherIndex(courseId, teacherId));
        
        for (Person person : persons) {
            if (person.getId() == teacherId) {
                return person;
            }
        }
        
        throw new DataNotFoundException("Can't get teacher! There is no person with ID " + teacherId);
    }
    
    /**
     * Gets the list index of the teacher with id
     * @param id  Which teacher
     * @return  index
     */
    public int getTeacherIndex(long courseId, long teacherId) throws DataNotFoundException {
        List<Long> teachers = this.courses.get(getCourseIndex(courseId)).getTeachers();
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i) == teacherId) {
                return i;
            }
        }
        
        throw new DataNotFoundException("Teacher with id = " + Long.toString(teacherId) +  " not found.");
    }
    
    
    /**
     * Adds a teacher to the course using the teacher's personal ID.
     * @param courseId
     * @param teacherId
     * @return
     * @throws BadInputException
     */
    public Person addTeacher(long courseId, long teacherId) throws BadInputException {
        for (Person person : persons) {
            if (person.getId() == teacherId) {
                // check if person is teacher
                if (!person.getRole().equals(PersonRoleMapper.personRoleToString(PersonRole.TEACHER))) {
                    throw new BadInputException("Can't add teacher! Person with this ID is not teacher!", "teacherId", Long.toString(teacherId));
                }
                
                courses.get(getCourseIndex(courseId)).addTeacher(teacherId);
                dbHandler.insertOrDeleteCourseStudentTeacher("insert",
                        PersonRoleMapper.personRoleToString(PersonRole.TEACHER), courseId, teacherId, false);
                return person;
            }
        }
        
        throw new DataNotFoundException("Can't add teacher! There is no person with ID " + teacherId);
    }
    
    /**
     * Removes a teacher from a course using the teacher's personal ID.
     * @param courseId
     * @param teacherId
     * @return
     */
    public Person removeTeacher(long courseId, long teacherId) {
        courses.get(getCourseIndex(courseId)).removeTeacher(getTeacherIndex(courseId, teacherId));
        dbHandler.insertOrDeleteCourseStudentTeacher("delete",
                PersonRoleMapper.personRoleToString(PersonRole.TEACHER), courseId, teacherId, false);

        for (Person person : persons) {
            if (person.getId() == teacherId) {
                return person;
            }
        }

        throw new DataNotFoundException("Can't return removed teacher! There is no person with ID " + teacherId);
    }
    
    // ----------------- STUDENTS -----------------
    
    /**
     * Gets all the students on the course.
     * @param courseId
     * @return
     */
    public List<Person> getStudents(long courseId) {
        List<Person> returner = new ArrayList<Person>();
        List<Long> courseStudents = courses.get(getCourseIndex(courseId)).getStudents();
        
        for (Person person : persons) {
            for (Long student : courseStudents) {
                if (student == person.getId()) {
                    returner.add(person);
                }
            }
        }
        return returner;
    }
    
    /**
     * Gets specific student on the course.
     * @param courseId
     * @param studentId
     * @return
     */
    public Person getStudent(long courseId, long studentId) {
        courses.get(getCourseIndex(courseId)).getStudents().get(getStudentIndex(courseId, studentId));
        
        for (Person person : persons) {
            if (person.getId() == studentId) {
                return person;
            }
        }
        
        throw new DataNotFoundException("Can't get student! There is no person with ID " + studentId);
    }
    
    /**
     * Gets the list index of the student with id
     * @param id  Which student
     * @return  index
     */
    public int getStudentIndex(long courseId, long studentId) throws DataNotFoundException {
        List<Long> students = this.courses.get(getCourseIndex(courseId)).getStudents();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i) == studentId) {
                return i;
            }
        }
        
        throw new DataNotFoundException("Student with id = " + Long.toString(studentId) +  " not found.");
    }
    
    /**
     * Adds a student to the course using the student's personal ID.
     * @param courseId
     * @param studentId
     * @return
     * @throws BadInputException
     */
    public Person addStudent(long courseId, long studentId) throws BadInputException {
        for (Person person : persons) {
            if (person.getId() == studentId) {
                // check if person is student
                if (!person.getRole().equals(PersonRoleMapper.personRoleToString(PersonRole.STUDENT))) {
                    System.out.println("DEBUG: " + person.getRole());
                    throw new BadInputException("Can't add student! Person with this ID is not student!", "studentId", Long.toString(studentId));
                }
                
                courses.get(getCourseIndex(courseId)).addStudent(studentId);
                dbHandler.insertOrDeleteCourseStudentTeacher("insert",
                        PersonRoleMapper.personRoleToString(PersonRole.STUDENT), courseId, studentId, false);
                return person;
            }
        }
        
        throw new DataNotFoundException("Can't add student! There is no person with ID " + studentId);
    }
    
    /**
     * Removes a student from a course using the student's personal ID.
     * @param courseId
     * @param studentId
     * @return
     */
    public Person removeStudent(long courseId, long studentId) {
        courses.get(getCourseIndex(courseId)).removeStudent(getStudentIndex(courseId, studentId));
        dbHandler.insertOrDeleteCourseStudentTeacher("delete",
                PersonRoleMapper.personRoleToString(PersonRole.STUDENT), courseId, studentId, false);

        for (Person person : persons) {
            if (person.getId() == studentId) {
                return person;
            }
        }

        throw new DataNotFoundException("Can't return removed student! There is no person with ID " + studentId);
    }
}
