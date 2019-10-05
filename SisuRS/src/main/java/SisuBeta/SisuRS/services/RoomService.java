 package SisuBeta.SisuRS.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import SisuBeta.SisuRS.classes.Building;
import SisuBeta.SisuRS.classes.Reservation;
import SisuBeta.SisuRS.classes.Room;
import SisuBeta.SisuRS.exceptions.BadInputException;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;
import SisuBeta.SisuRS.other.BuildingMapper;

public class RoomService {

    private ArrayList<Room> rooms = new ArrayList<Room>();
    private int nextId = 1;
    private int reservationNextId = 1;
    
    public RoomService() {
     // DEBUG: add rooms
//        Room r1 = addRoom(5, "A", 50, "no desc");
//        r1.addReservation(new Reservation(1, LocalDateTime.now(), LocalDateTime.now()));
//        r1.addReservation(new Reservation());
//        
//        Room r2 = addRoom(10, "B", 150, "no desc");
//        r2.addReservation(new Reservation());
    }
    
    
    /**
     * Returns all the rooms
     * @return  List of rooms
     */
    public ArrayList<Room> getRooms() {
        return this.rooms;
    }
    
    
    /**
     * Returns the room with given id
     * @param id  Id of room to search
     * @return  Room 
     */
    public Room getRoom(long id) throws DataNotFoundException {
        for (Room room : this.rooms) {
            if (room.getId() == id) {
                return room;
            }
        }
        // Room was not found so raise an exception
        throw new DataNotFoundException("Room with id = " + Long.toString(id) +  " not found.");
    }
  

    /**
     * Gets the list index of the room with id
     * @param id  Which room
     * @return  index
     */
    public int getRoomIndex(long id) throws DataNotFoundException {
  
        for (int i = 0; i < this.rooms.size(); i++) {
            if (rooms.get(i).getId() == id) {
                return i;
        }
    }
        // Room was not found so raise an exception
        throw new DataNotFoundException("Room with id = " + Long.toString(id) +  " not found.");
    }
    
    
    /**
     * Adds the room to service's room list
     * @param number
     * @param building 
     * @param capacity
     * @param description
     * @return Newly created room
     */
    public Room addRoom(int number, String building, int capacity, String description) {
        // checking proper values can be maybe withdrawn to separate method
        if (BuildingMapper.stringToBuilding(building) == Building.UNKNOWN) {
            throw new BadInputException("Value of 'building' can be only capital letter A-X!", "building", building);
        }
        
        long newId = this.nextId++;
        Room room = new Room(newId, number, building, capacity, description);
        this.rooms.add(room);
        return room;
    }

    /**
     * Adds the room to service's room list
     * @param newRoom  The new room to be added
     * @return Newly added room
     */
    public Room addRoom(Room newRoom) {
        // checking proper values can be maybe withdrawn to separate method
        if (BuildingMapper.stringToBuilding(newRoom.getBuilding()) == Building.UNKNOWN) {
            throw new BadInputException("Value of 'building' can be only capital letter A-X!", "building", newRoom.getBuilding());
        }
        
        long newId = this.nextId++;
        newRoom.setId(newId);
        this.rooms.add(newRoom);
        return newRoom;
    }
    
      
    /**
     * Updates the room's info
     * @param id  Which room to update
     * @param updatedRoom  Updated room 
     * @return Updated room
     */
    public Room updateRoom(long id, Room updatedRoom) throws DataNotFoundException {
        // checking proper values can be maybe withdrawn to separate method
        if (BuildingMapper.stringToBuilding(updatedRoom.getBuilding()) == Building.UNKNOWN) {
            throw new BadInputException("Value of 'building' can be only capital letter A-X!", "building", updatedRoom.getBuilding());
        }
        
        int index = getRoomIndex(id);
        this.rooms.set(index, updatedRoom);
        return updatedRoom;
    }
    
    
    /**
     * Updates the room's info. Room should contain id.
     * @param updatedRoom  Updated room with id of the old one
     * @return Updated room
     */
    public Room updateRoom(Room updatedRoom) throws DataNotFoundException {
        // checking proper values can be maybe withdrawn to separate method
        if (BuildingMapper.stringToBuilding(updatedRoom.getBuilding()) == Building.UNKNOWN) {
            throw new BadInputException("Value of 'building' can be only capital letter A-X!", "building", updatedRoom.getBuilding());
        }
        
        int index = getRoomIndex(updatedRoom.getId());
        this.rooms.set(index, updatedRoom);
        return updatedRoom;
    }
    
    /**
     * Removes the room with a given id
     * @param id  Which room to remove
     */
    public void removeRoom(long id) {
        this.rooms.removeIf(x -> x.getId() == id);
    }
    
    
// ----------------- RESERVATIONS -----------------
    
    
    /**
     * Get all reservations for a room.
     * @param roomId
     * @return
     */
    public List<Reservation> getReservations(long roomId) {
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                return room.getReservations();
            }
        }
        throw new BadInputException("The specified room does not exist.","room id", Long.toString(roomId));
    }
    
    public Reservation getReservation(long roomId, long reservationId) {
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                for (Reservation reservation : room.getReservations()) {
                    if (reservation.getId() == reservationId) {
                        return reservation;
                    }
                }
            }
        }
        
        throw new BadInputException("The specified reservation does not exist.","room id", Long.toString(reservationId));
    }
    
    
    /**
     * Add a reservation to a room using parameters.
     * @param roomId
     * @param courseId
     * @param startTime
     * @param endTime
     * @return
     */
    public Reservation addReservation(long roomId, long courseId, LocalDateTime startTime, LocalDateTime endTime) {
        
        return addReservation(roomId, new Reservation(courseId, startTime, endTime));
    }
    
    
    /**
     * Add a reservation to a room using a new room object.
     * @param roomId
     * @param newReservation
     * @return
     */
    public Reservation addReservation(long roomId, Reservation newReservation) {
        
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                newReservation.setId(reservationNextId++);
                return room.addReservation(newReservation);
            }
        }
        throw new BadInputException("Can't add reservation - the specified room does not exist.","room id", Long.toString(roomId));
    }
    
    
    /**
     * Update an existing reservation using a reservation object.
     * @param roomId
     * @param updatedReservation
     * @return
     */
    public Reservation updateReservation(long roomId, long reservationId, Reservation updatedReservation) {
        
        // Update.
        return getRoom(roomId).updateReservation(reservationId, updatedReservation);
    }
    
    
    /**
     * Remove a reservation from the specified room.
     * @param roomId
     * @param reservationId
     */
    public void removeReservation(long roomId, long reservationId) {
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                room.removeReservation(reservationId);
            }
        }
        throw new BadInputException("Can't remove reservation - the specified room does not exist.","room id", Long.toString(roomId));
    }
}
