package com.tbz.mntn.flattie.authentification;

import android.content.Context;

import com.tbz.mntn.flattie.database.dao.DaoFactory;
import com.tbz.mntn.flattie.database.dao.UserDao;
import com.tbz.mntn.flattie.database.dataclasses.User;
import com.tbz.mntn.flattie.internet.InternetChecker;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Login verification and handler.
 */
public class LoginController {

  /**
   * Check username and password and log user in if check successful.
   *
   * @param username from the user input
   * @param password from the user input
   * @return -3 if there is no internet connection, -2 if username doesn't exist,
   *        -1 if password wrong and 1 if login successful
   */
  public int login(String username, String password, Context context) {
    if (InternetChecker.isInternetConnectionActive(context)) {
      UserDao userDao = DaoFactory.getUserDao();
      User user = userDao.selectByUsername(username);

      if (!(user == null)) {
        if (arePasswordsMatching(user.getPassword(), password)) {
          LoggedInUser.setLoggedInUser(user);
          return 1;
        }
        return -1;
      }
      return -2;
    }
    return -3;
  }

  private Boolean arePasswordsMatching(String userPassword, String password) {
    return BCrypt.checkpw(password, userPassword);
  }
}
