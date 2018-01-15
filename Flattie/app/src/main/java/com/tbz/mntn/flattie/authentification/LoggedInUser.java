package com.tbz.mntn.flattie.authentification;

import com.tbz.mntn.flattie.database.dataclasses.User;

/**
 * Static class to keep a record of which User is logged in.
 */
public class LoggedInUser {
  private static User    loggedInUser;
  private static boolean loggedIn;

  public static User getLoggedInUser() {
    return loggedInUser;
  }

  public static void setLoggedInUser(User loggedInUser) {
    LoggedInUser.loggedInUser = loggedInUser;
    LoggedInUser.loggedIn = true;
  }

  public static void logoutCurrentlyLoggedInUser() {
    LoggedInUser.loggedInUser = null;
    LoggedInUser.loggedIn = false;
  }

  public static boolean isLoggedIn() {
    return loggedIn;
  }
}
