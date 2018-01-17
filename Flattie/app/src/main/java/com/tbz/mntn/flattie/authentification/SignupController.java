package com.tbz.mntn.flattie.authentification;

import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.Snackbar;

import android.view.View;

import com.tbz.mntn.flattie.authentification.LoggedInUser;
import com.tbz.mntn.flattie.database.dao.DaoFactory;
import com.tbz.mntn.flattie.database.dao.UserDao;
import com.tbz.mntn.flattie.database.dataclasses.Group;
import com.tbz.mntn.flattie.database.dataclasses.User;
import com.tbz.mntn.flattie.internet.InternetChecker;

import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Signup verification and handler.
 */
public class SignupController {

  /**
   * Check if username already exists.
   * If not create a new user account and log in.
   * @param name        from the user input
   * @param email       from the user input
   * @param password    from the user input
   * @param repPassword from the user input
   * @return true if user could log in, false if there was a problem with the login
   */
  public int signup(String name, String email, String password, String repPassword, Context context) {
    if(InternetChecker.isInternetConnectionActive(context)){

    }
    if (name.equals("")) {
      return -1;
    } else if (email.equals("")) {
      return -2;
    } else if (password.equals("")) {
      return -3;
    } else if (repPassword.equals("")) {
      return -4;
    } else if(!validate(email)){
      return -5;
    }
    if (password.equals(repPassword)) {
      UserDao userDao = DaoFactory.getUserDao();

      String salt = BCrypt.gensalt();
      String passwordHash = BCrypt.hashpw(password, salt);
      //group is by default null, afterwards it has to be changed
      //TODO: change group value to null if implementation of grupt-set is done
      Group group = new Group();
      group.setId(1);
      User   newUser      = new User(email, name, passwordHash, null, group);
      int    rows         = userDao.insert(newUser);

      if (rows > 0) {
        LoggedInUser.setLoggedInUser(newUser);
        return 1;
      } else if (rows == -200) {
        return -6;
      }
      return -7;
    }
    return -8;
  }

  public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

  public static boolean validate(String emailStr) {
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
    return matcher.find();
  }
}