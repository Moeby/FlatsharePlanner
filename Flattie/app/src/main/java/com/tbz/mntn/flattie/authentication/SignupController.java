package com.tbz.mntn.flattie.authentication;

import android.support.design.widget.Snackbar;

import com.tbz.mntn.flattie.database.dataclasses.User;

import android.support.design.widget.Snackbar;
import android.view.View;

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
   * @param name        from the user input
   * @param email       from the user input
   * @param password    from the user input
   * @param repPassword from the user input
   * @return true if user could log in, false if there was a problem with the login
   */
  //public int signup(String name, String email, String password, String repPassword, View view) {
  public Boolean signup(String name, String email, String password, String repPassword, View view) {
    if (name.equals("")) {
      Snackbar.make(view, "Please enter a name.", 3000).show();
      return false;
    } else if (email.equals("")) {
      Snackbar.make(view, "Please enter an email address.", 3000).show();
      return false;
    } else if (password.equals("")) {
      Snackbar.make(view, "Please enter a password.", 3000).show();
      return false;
    } else if (repPassword.equals("")) {
      Snackbar.make(view, "Please repeat your password.", 3000).show();
      return false;
    }
    if (password.equals(repPassword)) {
      UserDao userDAO = DaoFactory.getUserDao();

      //TODO: hash password and generate pepper
      //String passwordHash = BCrypt.hashpw(password);
      User newUser = new User(email, name, password, null, null);
      int  rows    = userDAO.insert(newUser);
      if (rows > 0) {
        LoggedInUser.setLoggedInUser(newUser);
        return true;
      } else if (rows == -200) {
        Snackbar.make(view, "Name or email address already in use. Please chose another one.", 3000).show();
        return false;
      }
      Snackbar.make(view, "Creation of a new user account failed.", 3000).show();
      return false;
    }
    Snackbar.make(view, "The repeat password does not match your password.", 3000).show();
    return false;
  }
}