 package SisuBeta.SisuRS.services;

import java.util.ArrayList;

import SisuBeta.SisuRS.classes.Room;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;

public class RoomService {

    private ArrayList<Room> rooms = new ArrayList<Room>();
    private int nextId = 1;
    
    public RoomService() {}
    
    
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
        throw new DataNotFoundException("Room with id = " + Long.toString(id) +  "not found.");
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
        long newId = this.nextId++;
        newRoom.setId(newId);
        this.rooms.add(newRoom);
        return newRoom;
    }
    
    
    /**
     * Removes the room with a given id
     * @param id  Which room to remove
     */
    public void removeRoom(long id) {
       this.rooms.removeIf(x -> x.getId() == id);
    }
    
      
    /**
     * Updates the room's info
     * @param id  Which room to update
     * @param updatedRoom  Updated room 
     * @return Updated room
     */
    public Room updateRoom(long id, Room updatedRoom) throws DataNotFoundException {
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
        int index = getRoomIndex(updatedRoom.getId());
        this.rooms.set(index, updatedRoom);
        return updatedRoom;
    }
    
    
    
    
    
    
    
}
