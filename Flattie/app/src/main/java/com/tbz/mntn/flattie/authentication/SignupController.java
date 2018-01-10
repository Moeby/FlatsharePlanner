package com.tbz.mntn.flattie.authentication;

import com.tbz.mntn.flattie.database.dao.DAOFactory;
import com.tbz.mntn.flattie.database.dao.UserDAO;
import com.tbz.mntn.flattie.database.dataclasses.User;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by natal on 09.01.2018.
 */

public class SignupController {
    /**
     * Check username and password and log user in if check successful
     *
     * @param name from the user input
     * @param password from the user input
     * @return null if username doesn't exist, false if password wrong and true if login successful
     */

    /**
     * Check if username already exists
     * if not create a new user account and log in
     *
     * @param name
     * @param email
     * @param password
     * @param salt
     * @return true if user could log in, false if there is already a user with this name and null if the user could not be inserted
     * TODO: alter user table, add column salt
     * TODO: check how to use BCrypt.checkpw and BCrypt.hashpw, why the salt is not used in BCrypt.checkpw?
     */
    public Boolean signup(String name, String email, String password, String salt){
        UserDAO userDAO = DAOFactory.getUserDAO();
        User user = userDAO.selectByUsername(name);

        if (user == null){
            User newUser = new User(email, name, password, null, null);
            int rows = userDAO.insert(newUser);
            if (rows > 0){
                LoggedInUser.setLoggedInUser(newUser);
                return true;
            }
            return null;
        }
        return false;
    }
}
