package SisuBeta.SisuRS.classes;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course {

    private long id = -1; // Unique ID for internal use.
    private String courseName;
    private String courseCode;
    private String courseDescription;
    private int courseYear;
    private int coursePeriod;
    private int courseCapacity;
    
    private ArrayList<Long> courseTeachers = new ArrayList<>();
    private ArrayList<Long> courseStudents = new ArrayList<>();
    
    /**
     * Default constructor.
     */
    public Course() {
    	super();
    }
    
    
    /**
     * Main constructor.
     * @param id
     * @param name
     * @param code
     * @param description
     * @param year
     * @param period
     * @param capacity
     */
    public Course(long id, String name, String code, String description, int year, int period, int capacity) {
        super();
        this.id = id;
        this.courseName = name;
        this.courseCode = code;
        this.courseDescription = description;
        this.courseYear = year;
        this.coursePeriod = period;
        this.courseCapacity = capacity;
    }
    
    
    /**
     * Checks if the course has valid data.
     * @return
     */
    public boolean isValid() {
    	return true;
    }

    
    // ID
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    // Name
    public String getName() { return courseName; }
    public void setName(String courseName) { this.courseName = courseName; }
    
    // Code
    public String getCode() { return courseCode; }
    public void setCode(String courseCode) { this.courseCode = courseCode; }
    
	// Description
    public String getDescription() { return courseDescription; }
    public void setDescription(String courseDescription) { this.courseDescription = courseDescription; }
    
	// Year
    public int getYear() { return courseYear; }
    public void setYear(int courseYear) { this.courseYear = courseYear; }
    
	// Period
    public int getPeriod() { return coursePeriod; }
    public void setPeriod(int coursePeriod) { this.coursePeriod = coursePeriod; }
    
    // Capacity
    public int getCapacity() { return courseCapacity; }
    public void setCapacity(int courseCapacity) { this.courseCapacity = courseCapacity; }
    
    // Teachers
    public ArrayList<Long> getTeachers() { return courseTeachers; }
    public void setTeachers(ArrayList<Long> courseTeachers) { this.courseTeachers = courseTeachers; }
    public boolean addTeacher(long personId) { return this.courseTeachers.add(personId); }
    public boolean removeTeacher(long personId) { return this.courseTeachers.remove(personId); }
    
	// Students
    public ArrayList<Long> getStudents() { return courseStudents; }
    public void setStudents(ArrayList<Long> courseStudents) { this.courseStudents = courseStudents; }
    public boolean addStudent(long personId) { return this.courseStudents.add(personId); }
    public boolean removeStudent(long personId) { return this.courseStudents.remove(personId); }
}
