package com.tbz.mntn.flattie.authentication;

import com.tbz.mntn.flattie.database.dao.DAOFactory;
import com.tbz.mntn.flattie.database.dao.UserDAO;
import com.tbz.mntn.flattie.database.dataclasses.User;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Login verification and handler
 */
public class LoginController {

    /**
     * Check username and password and log user in if check successful
     *
     * @param username from the user input
     * @param password from the user input
     * @return null if username doesn't exist, false if password wrong and true if login successful
     */
    public Boolean login(String username, String password){
        UserDAO userDAO = DAOFactory.getUserDAO();
        User user = userDAO.selectByUsername(username);

        if (!(user == null)){
            if (arePasswordsMatching(user.getPassword(), password)){
                LoggedInUser.setLoggedInUser(user);
                return true;
            }
            return false;
        }
        return null;
    }

    private Boolean arePasswordsMatching(String userPassword, String password){
        return BCrypt.checkpw(password, userPassword);
    }
}
