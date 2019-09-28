package SisuBeta.SisuRS.classes;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Room {

    private long id;
    private int number;
    private String building;
    private int capacity;
    private String description;
    private List<Integer> reservations = new ArrayList<>(); //This is gonna change later when reservations are implemented
    
    public Room() {}
    
    

    public Room(long id, int number, String building, int capacity, String description) {
        super();
        this.id = id;
        this.number = number;
        this.building = building;
        this.capacity = capacity;
        this.description = description;
    }



    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getReservations() {
        return reservations;
    }

    public void setReservations(List<Integer> reservations) {
        this.reservations = reservations;
    }
    
    
}
