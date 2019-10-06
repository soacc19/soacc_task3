package SisuBeta.SisuRS.services;

import java.util.ArrayList;
import java.util.List;

import SisuBeta.SisuRS.classes.User;
import SisuBeta.SisuRS.classes.UserRole;
import SisuBeta.SisuRS.db.DbHandler;
import SisuBeta.SisuRS.exceptions.BadInputException;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;
import SisuBeta.SisuRS.other.UserRoleMapper;

public class UserService {
    private List<User> users = new ArrayList<User>();
    private long nextId = 1;
    private DbHandler dbHandler = new DbHandler();
    
    /**
     * Default constructor.
     */
    public UserService() {
    }
    
    public void fillData() {
        users = dbHandler.selectUsers(null, true);
    }
    
    /**
     * Returns all users.
     * @return
     */
    public List<User> getUsers() {
        return this.users;
    }
    
    /**
     * Returns the user with the specified ID.
     * @param id
     * @return
     * @throws DataNotFoundException
     */
    public User getUser(long id) throws DataNotFoundException {
        for (User user : this.users) {
            if (user.getId() == id) {
                return user;
            }
        }
        
        throw new DataNotFoundException("User with id = " + Long.toString(id) +  " not found.");
    }
    
    /**
     * Returns the index of the user with the specified ID.
     * @param id
     * @return
     * @throws DataNotFoundException
     */
    public int getUserIndex(long id) throws DataNotFoundException {
        for (int i = 0; i < this.users.size(); i++) {
            if (users.get(i).getId() == id) {
                return i;
            }
        }
        
        throw new DataNotFoundException("User with id = " + Long.toString(id) +  " not found.");
    }
    
    /**
     * Adds a new user using a user object.
     * @param newUser
     * @return The added user object.
     * @throws BadInputException
     */
    public User addUser(User newUser) throws BadInputException {
        // Validate
        if (!(newUser.getRole().equals(UserRoleMapper.userRoleToString(UserRole.ADMIN))
                || newUser.getRole().equals(UserRoleMapper.userRoleToString(UserRole.USER)))) {
            throw new BadInputException("Value of attribute 'role' has to be 'admin' or 'user'!", "role", newUser.getRole());
        }
        
        // Add to DB
        long highestIdInTable = dbHandler.selectHighestIdFromTable("User");
        if (this.nextId <= highestIdInTable) {
            this.nextId = highestIdInTable + 1;
        }
        dbHandler.insertOrDeleteUser("insert", newUser, this.nextId);

        // Add locally
        newUser.setId(this.nextId++);
        this.users.add(newUser);
        
        return newUser;
    }
    
    /**
     * Removes user with the specified ID.
     * @param id
     * @throws DataNotFoundException
     */
    public void removeUser(long id) throws DataNotFoundException {
        dbHandler.insertOrDeleteUser("delete", null, id);
        
       if (!this.users.removeIf(x -> x.getId() == id)) {
           throw new DataNotFoundException("User with id = " + Long.toString(id) +  " not found.");
       }
    }
    
      
    /**
     * Updates a user with the specified ID using a new user object.
     * @param id
     * @param newUser
     * @return The updated user.
     * @throws BadInputException
     */
    public User updateUser(long id, User newUser) throws BadInputException {
        // Validate.
        if (!(newUser.getRole().equals(UserRoleMapper.userRoleToString(UserRole.ADMIN))
                || newUser.getRole().equals(UserRoleMapper.userRoleToString(UserRole.USER)))) {
            throw new BadInputException("Value of attribute 'role' has to be 'admin' or 'user'!", "role", newUser.getRole());
        }

        // Update DB
        dbHandler.insertOrDeleteUser("insert", newUser, id);
        
        // Update locally
        int index = getUserIndex(id);
        this.users.set(index, newUser);
        this.users.get(index).setId(id); // just in case if in update not expected ID was provided
        
        return newUser;
    }

}
