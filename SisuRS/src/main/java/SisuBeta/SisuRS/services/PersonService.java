package SisuBeta.SisuRS.services;

import java.util.ArrayList;

import SisuBeta.SisuRS.classes.Person;
import SisuBeta.SisuRS.classes.PersonRole;
import SisuBeta.SisuRS.exceptions.BadInputException;
import SisuBeta.SisuRS.exceptions.DataNotFoundException;
import SisuBeta.SisuRS.other.PersonRoleMapper;

public class PersonService {
    private ArrayList<Person> persons = new ArrayList<Person>();
    private int nextId = 1;

    public PersonService() {}

    /**
     * Returns all the persons
     * @return  List of persons
     */
    public ArrayList<Person> getPersons() {
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
    public Person addPerson(String name, String faculty, String email, String role) {
        // checking proper values can be maybe withdrawn to separate method
        if (PersonRoleMapper.stringToPersonRole(role) == PersonRole.UNKNOWN) {
            throw new BadInputException("Value of 'role' can be only 'student' or 'teacher'!", "role", role);
        }
        
        long newId = this.nextId++;
        Person person = new Person(newId, name, faculty, email, role);
        this.persons.add(person);
        return person;
    }

    /**
     * Adds the person to service's person list
     * @param newPerson  The new person to be added
     * @return Newly added person
     */
    public Person addPerson(Person newPerson) {
        // checking proper values can be maybe withdrawn to separate method
        if (PersonRoleMapper.stringToPersonRole(newPerson.getRole()) == PersonRole.UNKNOWN) {
            throw new BadInputException("Value of 'role' can be only 'student' or 'teacher'!", "role", newPerson.getRole());
        }
        
        long newId = this.nextId++;
        newPerson.setId(newId);
        this.persons.add(newPerson);
        return newPerson;
    }
    
    /**
     * Removes the person with a given id
     * @param id  Which person to remove
     */
    public void removePerson(long id) {
        this.persons.removeIf(x -> x.getId() == id);
    }
    
      
    /**
     * Updates the person's info
     * @param id  Which person to update
     * @param updatedRoom  Updated person 
     * @return Updated person
     */
    public Person updatePerson(long id, Person updatedPerson) throws DataNotFoundException {
        // checking proper values can be maybe withdrawn to separate method
        if (PersonRoleMapper.stringToPersonRole(updatedPerson.getRole()) == PersonRole.UNKNOWN) {
            throw new BadInputException("Value of 'role' can be only 'student' or 'teacher'!", "role", updatedPerson.getRole());
        }
        
        int index = getPersonIndex(id);
        this.persons.set(index, updatedPerson);
        return updatedPerson;
    }
    
    /**
     * Updates the person's info. Person should contain id.
     * @param updatedPerson  Updated person with id of the old one
     * @return Updated person
     */
    public Person updatePerson(Person updatedPerson) throws DataNotFoundException {
        // checking proper values can be maybe withdrawn to separate method
        if (PersonRoleMapper.stringToPersonRole(updatedPerson.getRole()) == PersonRole.UNKNOWN) {
            throw new BadInputException("Value of 'role' can be only 'student' or 'teacher'!", "role", updatedPerson.getRole());
        }
        
        
        int index = getPersonIndex(updatedPerson.getId());
        this.persons.set(index, updatedPerson);
        return updatedPerson;
    }

}
