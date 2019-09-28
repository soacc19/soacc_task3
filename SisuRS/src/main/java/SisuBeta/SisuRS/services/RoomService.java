package SisuBeta.SisuRS.services;

import java.util.ArrayList;

import SisuBeta.SisuRS.classes.Room;

public class RoomService {

    private ArrayList<Room> rooms = new ArrayList<Room>();
    
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
    public Room getRoom(long id) {
        Room result = null; //TODO: See below
        for (Room room : this.rooms) {
            if (room.getId() == id) {
                result =  room;
                break;
            }
        }
        return result;
        //TODO: Raise exception because room not found!
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
        long newId = rooms.size() + 1;
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
        long newId = rooms.size() + 1;
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
    public Room updateRoom(long id, Room updatedRoom) {
        Room room = getRoom(id);
        room = updatedRoom;
        return room;
    }
    
    
    /**
     * Updates the room's info. Room should contain id.
     * @param updatedRoom  Updated room with id of the old one
     * @return Updated room
     */
    public Room updateRoom(Room updatedRoom) {
        Room room = getRoom(updatedRoom.getId());
        room = updatedRoom;
        return room;
    }
    
    
    
    
    
    
    
}
