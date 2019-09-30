package SisuBeta.SisuRS.classes;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import SisuBeta.SisuRS.exceptions.DataNotFoundException;

@XmlRootElement
public class Room {

    private long id;
    private int number;
    private String building;
    private int capacity;
    private String description;
    private List<Reservation> reservations = new ArrayList<>();
    private List<Link> links = new ArrayList<>(); // HATEOAS


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


    public List<Reservation> getReservations() {
        return reservations;
    }


    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


    public boolean addReservation(Reservation reservation) {
        return this.reservations.add(reservation);
    }


    public int getReservationIndex(long reservationId) throws DataNotFoundException {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId() == reservationId) {
                return i;
            }
        }
        // Room was not found so raise an exception
        throw new DataNotFoundException("Reservation with id = " + Long.toString(reservationId) + " not found.");
    }


    public Reservation updateReservation(long reservationId, Reservation updatedReservation) {
        return reservations.set(getReservationIndex(reservationId), updatedReservation);
    }


    public void removeReservation(long reservationId) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId() == reservationId) {
                reservations.remove(i);
            }
        }
    }


    public List<Link> getLinks() {
        return links;
    }


    public void setLinks(List<Link> links) {
        this.links = links;
    }


    public void addLink(String url, String rel) {
        Link link = new Link();
        link.setLink(url);
        link.setRel(rel);
        links.add(link);
    }

}
