 package SisuBeta.SisuRS.services;

import java.util.ArrayList;
import java.util.List;

import SisuBeta.SisuRS.classes.Building;
import SisuBeta.SisuRS.classes.Course;
import SisuBeta.SisuRS.classes.Reservation;
import SisuBeta.SisuRS.classes.Room;
import SisuBeta.SisuRS.db.DbHandler;
import SisuBeta.SisuRS.exceptions.BadInputException;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;
import SisuBeta.SisuRS.other.BuildingMapper;

public class RoomService {

    private List<Room> rooms = new ArrayList<Room>();
    private List<Course> courses = new ArrayList<Course>();
    private long nextId = 1;
    private long reservationNextId = 1;
    private DbHandler dbHandler = new DbHandler();
    
    public RoomService() {
    }
    
    public void fillData() {
        rooms = dbHandler.selectAllRooms();
        courses = dbHandler.selectAllCourses();
        
        for (Room room : rooms) {
            fillRoomWithReservations(room.getId());
        }
    }
    /**
     * Returns all the rooms
     * @return  List of rooms
     */
    public List<Room> getRooms() {
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
        // Validate
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
        // Validate
        if (BuildingMapper.stringToBuilding(newRoom.getBuilding()) == Building.UNKNOWN) {
            throw new BadInputException("Value of 'building' can be only capital letter A-X!", "building", newRoom.getBuilding());
        }
        
        // Add to DB
        long highestIdInTable = dbHandler.selectHighestIdFromTable("Room");
        if (this.nextId <= highestIdInTable) {
            this.nextId = highestIdInTable + 1;
        }
        dbHandler.insertOrDeleteRoom("insert", newRoom, this.nextId);
        
        // create and insert reservations
        if (!newRoom.getReservations().isEmpty()) {
            dbHandler.createOrDropRoomReservationTable("create", this.nextId);
            for (Reservation reservation : newRoom.getReservations()) {
                dbHandler.insertOrDeleteRoomReservation("insert", this.nextId, reservation, reservation.getId(), false);
            }
        }
        
        // Add locally
        newRoom.setId(this.nextId++);
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
        // Validate.
        if (BuildingMapper.stringToBuilding(updatedRoom.getBuilding()) == Building.UNKNOWN) {
            throw new BadInputException("Value of 'building' can be only capital letter A-X!", "building", updatedRoom.getBuilding());
        }
        int index = getRoomIndex(id);
        
        // Update DB
        dbHandler.insertOrDeleteRoom("insert", updatedRoom, id);
        dbHandler.insertOrDeleteRoomReservation("delete", id, null, 0, true);
        for (Reservation reservation : updatedRoom.getReservations()) {
            dbHandler.insertOrDeleteRoomReservation("insert", id, reservation, reservation.getId(), false);
        }
        
        // Update locally
        this.rooms.set(index, updatedRoom);
        this.rooms.get(index).setId(id); // just in case if in update not expected ID was provided
        return updatedRoom;
    }
    
//  TODO Since we providing update only by PUT with ID param in URL, this is not useful
//    /**
//     * Updates the room's info. Room should contain id.
//     * @param updatedRoom  Updated room with id of the old one
//     * @return Updated room
//     */
//    public Room updateRoom(Room updatedRoom) throws DataNotFoundException {
//        // Validate.
//        if (BuildingMapper.stringToBuilding(updatedRoom.getBuilding()) == Building.UNKNOWN) {
//            throw new BadInputException("Value of 'building' can be only capital letter A-X!", "building", updatedRoom.getBuilding());
//        }
//        
//        // Update DB
//        dbHandler.insertOrDeleteRoom("insert", updatedRoom, id);
//        dbHandler.insertOrDeleteRoomReservation("delete", id, null, true);
//        for (Reservation reservation : updatedRoom.getReservations()) {
//            dbHandler.insertOrDeleteRoomReservation("insert", id, reservation, false);
//        }
//        
//        // Update locally
//        int index = getRoomIndex(id);
//        this.rooms.set(index, updatedRoom);
//        this.rooms.get(index).setId(id); // just in case if in update not expected ID was provided
//        return updatedRoom;
//    }
    
    /**
     * Removes the room with a given id
     * @param id  Which room to remove
     */
    public void removeRoom(long id) {
        // Validate
        rooms.get(getRoomIndex(id));
        
        // Remove from DB
        dbHandler.insertOrDeleteRoom("delete", null, id);
        dbHandler.createOrDropRoomReservationTable("drop", id);
        
        // Remove locally
        if (!this.rooms.removeIf(x -> x.getId() == id)) {
            throw new DataNotFoundException("Room with id = " + Long.toString(id) +  " not found.");
        }
    }
    
    public boolean fillRoomWithReservations(long id) {
        rooms.get(getRoomIndex(id)).setReservations(dbHandler.selectAllRoomReservations(id));
        return true;
    }
    
    
// ----------------- RESERVATIONS -----------------
    
    
    /**
     * Get all reservations for a room.
     * @param roomId
     * @return
     */
    public List<Reservation> getReservations(long roomId) {
        return rooms.get(getRoomIndex(roomId)).getReservations();
    }
    
    public Reservation getReservation(long roomId, long reservationId) {
        return rooms.get(getRoomIndex(roomId)).getReservations().get(getReservationIndex(roomId, reservationId));
    }
    
    /**
     * Gets the list index of the teacher with id
     * @param id  Which teacher
     * @return  index
     */
    public int getReservationIndex(long roomId, long reservationId) throws DataNotFoundException {
        List<Reservation> reservations = this.rooms.get(getRoomIndex(roomId)).getReservations();
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId() == reservationId) {
                return i;
            }
        }
        
        throw new DataNotFoundException("Reservation with id = " + Long.toString(reservationId) +  " not found.");
    }
    
    
    /**
     * Add a reservation to a room using parameters.
     * @param roomId
     * @param courseId
     * @param startTime
     * @param endTime
     * @return
     */
//    public Reservation addReservation(long roomId, long courseId, LocalDateTime startTime, LocalDateTime endTime) {
//        
//        return addReservation(roomId, new Reservation(courseId, startTime, endTime));
//    }
    
    
    /**
     * Add a reservation to a room using a new room object.
     * @param roomId
     * @param newReservation
     * @return
     */
    public Reservation addReservation(long roomId, Reservation newReservation) {
        // Validate
        Room room = rooms.get(getRoomIndex(roomId));
        boolean validCourse = false;
        for (Course course : courses ) {
            if (course.getId() == newReservation.getCourseId()) {
                validCourse = true;
                break;
            }
        }
        if (!validCourse) {
            throw new DataNotFoundException("Can't add reservation! There is no course with ID " + newReservation.getCourseId());
        }
        
        // Add to DB
        long highestIdInTable = dbHandler.selectHighestIdFromTable("Room_" + roomId + "_reservations");
        if (this.reservationNextId <= highestIdInTable) {
            this.reservationNextId = highestIdInTable + 1;
        }
        newReservation.setId(this.reservationNextId++);
        dbHandler.insertOrDeleteRoomReservation("insert", roomId, newReservation, newReservation.getId(),false);

        // Update locally
        return room.addReservation(newReservation);
    }
    
    
    /**
     * Update an existing reservation using a reservation object.
     * @param roomId
     * @param updatedReservation
     * @return
     */
    public Reservation updateReservation(long roomId, long reservationId, Reservation updatedReservation) {
        // Validate.
        int index = getReservationIndex(roomId, reservationId);
        Room room = rooms.get(getRoomIndex(roomId));
        boolean validCourse = false;
        for (Course course : courses ) {
            if (course.getId() == updatedReservation.getCourseId()) {
                validCourse = true;
                break;
            }
        }
        if (!validCourse) {
            throw new DataNotFoundException("Can't add reservation! There is no course with ID " + updatedReservation.getCourseId());
        }
        
        // Update DB
        dbHandler.insertOrDeleteRoomReservation("insert", roomId, updatedReservation, reservationId, false);
        
        // Update locally
        return room.updateReservation(index, updatedReservation);
    }
    
    
    /**
     * Remove a reservation from the specified room.
     * @param roomId
     * @param reservationId
     */
    public void removeReservation(long roomId, long reservationId) {
        // Validate.
        rooms.get(getRoomIndex(roomId)).removeReservation(getReservationIndex(roomId, reservationId));
        
        dbHandler.insertOrDeleteRoomReservation("delete", roomId, null, reservationId, false);
    }
}
