package SisuBeta.SisuRS.classes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course {

    private long id = -1; // Unique ID for internal use.
    private String name;
    private String code;
    private String description;
    private int year;
    private int period;
    private int capacity;
    
    private List<Long> teachers = new ArrayList<>();
    private List<Long> students = new ArrayList<>();
    
    private List<Link> links= new ArrayList<>(); // HATEOAS
    
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
        this.name = name;
        this.code = code;
        this.description = description;
        this.year = year;
        this.period = period;
        this.capacity = capacity;
    }
    
    // ID
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    // Name
    public String getName() { return name; }
    public void setName(String courseName) { this.name = courseName; }
    
    // Code
    public String getCode() { return code; }
    public void setCode(String courseCode) { this.code = courseCode; }
    
	// Description
    public String getDescription() { return description; }
    public void setDescription(String courseDescription) { this.description = courseDescription; }
    
	// Year
    public int getYear() { return year; }
    public void setYear(int courseYear) { this.year = courseYear; }
    
	// Period
    public int getPeriod() { return period; }
    public void setPeriod(int coursePeriod) { this.period = coursePeriod; }
    
    // Capacity
    public int getCapacity() { return capacity; }
    public void setCapacity(int courseCapacity) { this.capacity = courseCapacity; }
    
    // Teachers
    public List<Long> getTeachers() { return teachers; }
    public void setTeachers(List<Long> courseTeachers) { this.teachers = courseTeachers; }
    public boolean addTeacher(long personId) { return this.teachers.add(personId); }
    public Long removeTeacher(int index) { return this.teachers.remove(index); }
    
	// Students
    public List<Long> getStudents() { return students; }
    public void setStudents(List<Long> list) { this.students = list; }
    public boolean addStudent(long personId) { return this.students.add(personId); }
    public Long removeStudent(int index) { return this.students.remove(index); }
    
    public List<Link> getLinks() {
        return links;
    }
    
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addLink(String url, String rel) {
        Link link = new Link();
        link.setHref(url);
        link.setRel(rel);
        links.add(link);
    }
}
