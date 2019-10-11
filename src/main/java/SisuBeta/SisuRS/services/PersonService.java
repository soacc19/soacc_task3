package SisuBeta.SisuRS.services;

import java.util.ArrayList;
import java.util.List;

import SisuBeta.SisuRS.classes.Person;
import SisuBeta.SisuRS.classes.PersonRole;
import SisuBeta.SisuRS.db.DbHandler;
import SisuBeta.SisuRS.exceptions.BadInputException;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;
import SisuBeta.SisuRS.other.PersonRoleMapper;

public class PersonService {
    private List<Person> persons = new ArrayList<Person>();
    private long nextId = 1;
    private DbHandler dbHandler = new DbHandler();

    public PersonService() {}
    
    public void fillData() {
        persons = dbHandler.selectAllPersons();
    }

    /**
     * Returns all the persons
     * @return  List of persons
     */
    public List<Person> getPersons() {
        return this.persons;
    }

    /**
     * Returns the person with given id
     * @param id  Id of person to search
     * @return  Person 
     */
    public Person getPerson(long id) throws DataNotFoundException {
        for (Person person : this.persons) {
            if (person.getId() == id) {
                return person;
            }
        }
        // Person was not found so raise an exception
        throw new DataNotFoundException("Person with id = " + Long.toString(id) + " not found.");
    }
    
    /**
     * Gets the list index of the person with id
     * @param id  Which person
     * @return  index
     */
    public int getPersonIndex(long id) throws DataNotFoundException {
        for (int i = 0; i < this.persons.size(); i++) {
            if (persons.get(i).getId() == id) {
                return i;
            }
        }
        // Person was not found so raise an exception
        throw new DataNotFoundException("Person with id = " + Long.toString(id) +  " not found.");
    }
    
    /**
     * Adds the person to service's person list
     * @param name
     * @param faculty 
     * @param email
     * @param role
     * @return Newly created person
     */
//    public Person addPerson(String name, String faculty, String email, String role) {
//        // Validate
//        if (PersonRoleMapper.stringToPersonRole(role) == PersonRole.UNKNOWN) {
//            throw new BadInputException("Value of 'role' can be only 'student' or 'teacher'!", "role", role);
//        }
//        
//        long newId = this.nextId++;
//        Person person = new Person(newId, name, faculty, email, role);
//        this.persons.add(person);
//        return person;
//    }

    /**
     * Adds the person to service's person list
     * @param newPerson  The new person to be added
     * @return Newly added person
     */
    public Person addPerson(Person newPerson) {
        // Validate
        if (PersonRoleMapper.stringToPersonRole(newPerson.getRole()) == PersonRole.UNKNOWN) {
            throw new BadInputException("Value of 'role' can be only 'student' or 'teacher'!", "role", newPerson.getRole());
        }
        
        // Add to DB
        long highestIdInTable = dbHandler.selectHighestIdFromTable("Person");
        if (this.nextId <= highestIdInTable) {
            this.nextId = highestIdInTable + 1;
        }
        dbHandler.insertOrDeletePerson("insert", newPerson, this.nextId);
        
        // Add locally
        newPerson.setId(this.nextId++);
        this.persons.add(newPerson);
        return newPerson;
    }
    
    /**
     * Updates the person's info
     * @param id  Which person to update
     * @param updatedRoom  Updated person 
     * @return Updated person
     */
    public Person updatePerson(long id, Person updatedPerson) throws DataNotFoundException {
        // Validate.
        if (PersonRoleMapper.stringToPersonRole(updatedPerson.getRole()) == PersonRole.UNKNOWN) {
            throw new BadInputException("Value of 'role' can be only 'student' or 'teacher'!", "role", updatedPerson.getRole());
        }
        int index = getPersonIndex(id);
        
        // Update DB
        dbHandler.insertOrDeletePerson("insert", updatedPerson, id);
        
        // Update locally
        this.persons.set(index, updatedPerson);
        this.persons.get(index).setId(id); // just in case if in update not expected ID was provided
        return updatedPerson;
    }

//  TODO Since we providing update only by PUT with ID param in URL, this is not useful
//    /**
//     * Updates the person's info. Person should contain id.
//     * @param updatedPerson  Updated person with id of the old one
//     * @return Updated person
//     */
//    public Person updatePerson(Person updatedPerson) throws DataNotFoundException {
//        // checking proper values can be maybe withdrawn to separate method
//        if (PersonRoleMapper.stringToPersonRole(updatedPerson.getRole()) == PersonRole.UNKNOWN) {
//            throw new BadInputException("Value of 'role' can be only 'student' or 'teacher'!", "role", updatedPerson.getRole());
//        }
//        
//        
//        int index = getPersonIndex(updatedPerson.getId());
//        this.persons.set(index, updatedPerson);
//        return updatedPerson;
//    }
    
    /**
     * Removes the person with a given id
     * @param id  Which person to remove
     */
    public void removePerson(long id) {
        // Validate
        persons.get(getPersonIndex(id));
        
        // Remove from DB
        dbHandler.insertOrDeletePerson("delete", null, id);
        
        // Remove locally
        if (!this.persons.removeIf(x -> x.getId() == id)) {
            throw new DataNotFoundException("Person with id = " + Long.toString(id) +  " not found.");
        }
    }
}
