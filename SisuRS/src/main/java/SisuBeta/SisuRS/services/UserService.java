package SisuBeta.SisuRS.services;

import java.util.ArrayList;
import java.util.List;

import SisuBeta.SisuRS.classes.User;

/**
 * Service for handling users
 *
 */
public class UserService {

    public User getUser(String username) {

        // TODO: Use db to get the user!
        List<String> roles = new ArrayList<String>();
        roles.add("admin");
        return new User("user", "password", roles);
    }

    // TODO: Basic CRUD for users needed?

}
