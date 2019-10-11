package SisuBeta.SisuRS.classes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Reservation {
    private long id;
    private long courseId = -1;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ArrayList<Long> attendants = new ArrayList<>();
    private List<Link> links= new ArrayList<>(); // HATEOAS
    
    
    /**
     * Default constructor.
     */
    public Reservation() {
    	super();
    }
    
    
    public Reservation(long id, long courseId, LocalDateTime startTime, LocalDateTime endTime) {
        super();
        this.id = id; // useless
        this.courseId = courseId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // ID
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
	// Course ID
    public long getCourseId() { return courseId; }
    public void setCourseId(long courseId) { this.courseId = courseId; }

    // Starting time
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    // Ending time
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    // Attendants
    public ArrayList<Long> getAttendants() { return attendants;  }
    public void setAttendants(ArrayList<Long> attendants) { this.attendants = attendants; }
    public boolean addAttendant(long personId) { return this.attendants.add(personId); }
    
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
