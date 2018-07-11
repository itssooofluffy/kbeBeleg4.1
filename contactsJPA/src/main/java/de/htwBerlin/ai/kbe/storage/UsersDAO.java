package de.htwBerlin.ai.kbe.storage;

import java.util.Collection;

import de.htwBerlin.ai.kbe.data.User;

public interface UsersDAO {

    /**
     * Retrieves a User
     * 
     * @param userId
     * @return
     */
    public User findUserById(String userId);

    /**
     * Retrieves all Users
     * 
     * @return
     */
    public Collection<User> findAllUsers();

    /**
     * Save a new User
     * 
     * @param User
     * @return the Id of the new Users
     */
    public int saveUser(User User);
    
    /**
     * Deletes the User for the provided id
     * @param id
     */
    public void deleteUser(String id);
}
