package com.tbz.mntn.flattie.authentication;

import com.tbz.mntn.flattie.database.dao.DaoFactory;
import com.tbz.mntn.flattie.database.dao.UserDao;
import com.tbz.mntn.flattie.database.dataclasses.User;

/**
 * Signup verification and handler
 */

public class SignupController {

  /**
   * Check if username already exists
   * if not create a new user account and log in
   *
   * @param name        from the user input
   * @param email       from the user input
   * @param password    from the user input
   * @param repPassword from the user input
   * @return true if user could log in, false if there is already a user with this name and null if the user could not be inserted
   * TODO: alter user table, add column salt
   * TODO: check how to use BCrypt.checkpw and BCrypt.hashpw, why the salt is not used in BCrypt.checkpw?
   */
  public int signup(String name, String email, String password, String repPassword) {
    if (password.equals(repPassword)) {
      UserDao userDao = DaoFactory.getUserDao();
      //User user = userDao.selectByUsername(name);

      //if (user == null) {
        User newUser = new User(email, name, password, null, null);
        int rows = userDao.insert(newUser);
        if (rows > 0) {
          LoggedInUser.setLoggedInUser(newUser);
          return 0;
        }
        return 1;
      //}
      //return 2;
    }
    return 3;
  }
}