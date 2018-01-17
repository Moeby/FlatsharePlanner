package com.tbz.mntn.flattie.authentification;

import android.content.Context;

import com.tbz.mntn.flattie.database.dao.DaoFactory;
import com.tbz.mntn.flattie.database.dao.UserDao;
import com.tbz.mntn.flattie.database.dataclasses.Group;
import com.tbz.mntn.flattie.database.dataclasses.User;
import com.tbz.mntn.flattie.internet.InternetChecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Signup verification and handler.
 */
public class SignupController {

  public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

  /**
   * Check if username already exists.
   * If not create a new user account and log in.
   *
   * @param name        from the user input
   * @param email       from the user input
   * @param password    from the user input
   * @param repPassword from the user input
   * @return 1 if user could log in, -1 if the email address does not match a cretain pattern,
   *        -2 if the username or email is already in use, -3 if the account could not be created,
   *        -4 if both passwords do not match each other and -5 if app does not have internet
   *        connection
   */
  public int signup(String name, String email, String password,
                    String repPassword, Context context) {
    if (InternetChecker.isInternetConnectionActive(context)) {
      if (!validate(email)) {
        return -1;
      }
      if (password.equals(repPassword)) {
        UserDao userDao = DaoFactory.getUserDao();

        String salt = BCrypt.gensalt();
        String passwordHash = BCrypt.hashpw(password, salt);
        //group is by default null, afterwards it has to be changed
        //TODO: change group value to null if implementation of grupt-set is done
        Group group = new Group();
        group.setId(1);
        User newUser = new User(email, name, passwordHash, null, group);
        int rows = userDao.insert(newUser);

        if (rows > 0) {
          LoggedInUser.setLoggedInUser(newUser);
          return 1;
        } else if (rows == -200) {
          return -2;
        }
        return -3;
      }
      return -4;
    }
    return -5;
  }

  public static boolean validate(String emailStr) {
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
    return matcher.find();
  }
}