package SisuBeta.SisuRS.classes;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Reservation {
	private static long uniqueID = 1;
    private long id = -1; // Unique ID for internal use.
    private long courseId = -1;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Object reserver;
    private ArrayList<Long> attendants = new ArrayList<>();
    
    
    /**
     * Default constructor.
     */
    public Reservation() {
    	super();
    }
    
    
    public Reservation(long courseId, LocalDateTime startTime, LocalDateTime endTime) {
        super();
        this.id = uniqueID++;
        this.courseId = courseId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    
    /**
     * 
     * @return
     */
    public boolean isValid() {
    	return true;
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
    
	// Reserver
    public Object getReserver() { return reserver; }
    public void setReserver(Object reserver) { this.reserver = reserver; }
    
    // Attendants
    public ArrayList<Long> getAttendants() { return attendants;  }
    public void setAttendants(ArrayList<Long> attendants) { this.attendants = attendants; }
    public boolean addAttendant(long personId) { return this.attendants.add(personId); }
}
