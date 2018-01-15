package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.databaseConnection.MysqlConnector;
import com.tbz.mntn.flattie.database.dataclasses.Group;
import com.tbz.mntn.flattie.database.dataclasses.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public class UserDao extends Dao {
  private static UserDao         instance = new UserDao();
  private        ArrayList<User> users    = new ArrayList();
  private ArrayList<User> userList;
  private User            user;
  private int             rows;

  // table constants
  private static final String TABLE        = "user";
  private static final String ID           = "id";
  private static final String EMAIL        = "email";
  private static final String USERNAME     = "username";
  private static final String PASSWORD     = "password";
  private static final String REMOVAL_DATE = "removal_date";
  private static final String GROUP_FK     = "group_fk";

  private UserDao() {}

  static UserDao getInstance() {
    return instance;
  }

  /**
   * Insert a new user into the database.
   * @param user required values: email, username, password<br>
   *             possible nullable: removal date and group with id
   * @return positive = ok, negative = error
   * @see Dao#switchSqlError(String method, SQLException e)
   */
  public int insert(final User user) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "insert " + TABLE;
            rows = -1;
            Connection        con    = getConnection(method);
            PreparedStatement stmt   = null;
            ResultSet         result = null;
            try {
              stmt = con.prepareStatement("INSERT INTO " + TABLE + " ("
                                          + EMAIL + ","
                                          + USERNAME + ","
                                          + PASSWORD + ","
                                          + REMOVAL_DATE + ","
                                          + GROUP_FK + ")"
                                          + " VALUES( ?, ?, ?, ?, ?);",
                                          Statement.RETURN_GENERATED_KEYS);
              stmt.setString(1, user.getEmail());
              stmt.setString(2, user.getUsername());
              stmt.setString(3, user.getPassword());
              stmt.setDate(4, user.getRemovalDate());
              Group group = user.getGroup();
              if (group != null && group.getId() != 0) {
                stmt.setInt(5, group.getId());
              } else {
                stmt.setNull(5, Types.INTEGER);
              }

              rows = stmt.executeUpdate();
              ResultSet generatedKeys = stmt.getGeneratedKeys();
              if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
              }

              if (rows > 0) {
                users.add(user);
              }

            } catch (SQLException e) {
              rows = switchSqlError(method, e);
            } finally {
              try {
                // free resources
                if (result != null) {
                  result.close();
                }
                if (stmt != null) {
                  stmt.close();
                }
                if (closeCon) {
                  con.close();
                }
              } catch (SQLException e) {
                logSqlError("closure " + method, e);
              }
            }
          }
        });
    try {
      thread.start();
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return rows;
  }

  /**
   * Gets a user from database.
   * @param username search criteria
   * @return user from database or null if not found / an error occurred <br>ignores removed users
   */
  public User selectByUsername(final String username) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String            method = "selectByUsername " + TABLE;
            Connection        con    = getConnection(method);
            PreparedStatement stmt   = null;
            ResultSet         result = null;
            try {
              stmt = con.prepareStatement("SELECT "
                                          + ID + ","
                                          + EMAIL + ","
                                          + PASSWORD + ","
                                          + REMOVAL_DATE + ","
                                          + GROUP_FK
                                          + " FROM " + TABLE
                                          + " WHERE " + USERNAME + " = ?"
                                          + " AND " + REMOVAL_DATE + " IS NULL;");
              stmt.setString(1, username);
              result = stmt.executeQuery();
              if (result.next()) {
                for (User savedUser : users) {
                  if (username.equals(savedUser.getUsername())) {
                    setUser(savedUser);
                    break;
                  }
                }
                if (user == null) {
                  setUser(new User());

                  int groupFk = result.getInt(GROUP_FK);
                  if (groupFk != 0) {
                    user.setGroup(DaoFactory.getGroupDao().selectById(groupFk));
                  } else {
                    users.add(user);
                  }
                }
                user.setUsername(username);
                user.setId(result.getInt(ID));
                user.setEmail(result.getString(EMAIL));
                user.setPassword(result.getString(PASSWORD));
                user.setRemovalDate(result.getDate(REMOVAL_DATE));
              }
            } catch (SQLException e) {
              logSqlError(method, e);
              MysqlConnector.close();
              user = null;
            } finally {
              try {
                // free resources
                if (result != null) {
                  result.close();
                }
                if (stmt != null) {
                  stmt.close();
                }
                if (closeCon) {
                  MysqlConnector.close();
                }
              } catch (SQLException e) {
                logSqlError("closure selectByUsername " + TABLE, e);
              }
            }
          }
        });

    try {
      thread.start();
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return user;
  }

  private void selectByEmail(String email) {
    // TODO LATER: implement method
  }

  /**
   * Get all users of a group.
   * @param group required values: id
   * @return list of users from database or null if not found / an error occurred
   * <br>ignores removed user
   */
  public ArrayList<User> selectAllByGroupId(final Group group) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "selectAllByGroupId " + TABLE;
            userList = new ArrayList();
            int               groupFk = group.getId();
            Connection        con     = getConnection(method);
            PreparedStatement stmt    = null;
            ResultSet         result  = null;
            try {
              stmt = con.prepareStatement("SELECT "
                                          + ID + ","
                                          + EMAIL + ","
                                          + PASSWORD + ","
                                          + REMOVAL_DATE + ","
                                          + USERNAME
                                          + " FROM " + TABLE
                                          + " WHERE " + GROUP_FK + " = ?"
                                          + " AND " + REMOVAL_DATE + " IS NULL;");
              stmt.setInt(1, groupFk);
              result = stmt.executeQuery();
              while (result.next()) {
                int  id   = result.getInt(ID);
                User user = null;
                for (User savedUser : users) {
                  if (id == savedUser.getId()) {
                    user = savedUser;
                    break;
                  }
                }
                if (user == null) {
                  user = new User();
                  users.add(user);
                }
                user.setId(id);
                user.setEmail(result.getString(EMAIL));
                user.setUsername(result.getString(USERNAME));
                user.setPassword(result.getString(PASSWORD));
                user.setRemovalDate(result.getDate(REMOVAL_DATE));
                user.setGroup(group);

                userList.add(user);
              }

            } catch (SQLException e) {
              logSqlError(method, e);
              MysqlConnector.close();
              userList = null;
            } finally {
              try {
                // free resources
                if (result != null) {
                  result.close();
                }
                if (stmt != null) {
                  stmt.close();
                }
                if (closeCon) {
                  MysqlConnector.close();
                }
              } catch (SQLException e) {
                logSqlError("closure " + method, e);
              }
            }
          }
        });

    try {
      thread.start();
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (!userList.isEmpty()) {
      return userList;
    } else {
      return null;
    }
  }

  /**
   * Update group_fk in user.
   * @param user needs to contain at least the id and a group with an id
   * @return positive = ok, negative = error
   * @see Dao#switchSqlError(String method, SQLException e)
   */
  public int updateGroup(final User user) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "updateGroup " + TABLE;
            Group  group  = user.getGroup();
            rows = -1;
            Connection        con    = getConnection(method);
            PreparedStatement stmt   = null;
            ResultSet         result = null;
            try {
              stmt = con.prepareStatement("UPDATE " + TABLE
                                          + " SET " + GROUP_FK + " = ?"
                                          + " WHERE " + ID + " = ?"
                                          + " AND " + REMOVAL_DATE + " IS NULL;");
              if (group != null && group.getId() != 0) {
                stmt.setInt(1, group.getId());
              } else {
                stmt.setNull(1, Types.INTEGER);
              }

              stmt.setInt(2, user.getId());

              rows = stmt.executeUpdate();

            } catch (SQLException e) {
              rows = switchSqlError(method, e);
            } finally {
              try {
                // free resources
                if (result != null) {
                  result.close();
                }
                if (stmt != null) {
                  stmt.close();
                }
                if (closeCon) {
                  MysqlConnector.close();
                }
              } catch (SQLException e) {
                logSqlError("closure " + method, e);
              }
            }
          }
        });

    try {
      thread.start();
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return rows;
  }

  /**
   * Update removal_date to current date - this user will be ignored by future selects.
   * @param user required values: id, group with id <br>
   *             ignored values: everything else
   * @return positive = ok, negative = error
   * @see Dao#switchSqlError(String method, SQLException e)
   */
  public int remove(final User user) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method      = "remove " + TABLE;
            Date   removalDate = new Date(new java.util.Date().getTime());
            rows = -1;
            Connection        con    = getConnection(method);
            PreparedStatement stmt   = null;
            ResultSet         result = null;
            try {
              stmt = con.prepareStatement("UPDATE " + TABLE
                                          + " SET " + REMOVAL_DATE + " = ?"
                                          + " WHERE " + ID + " = ?;");
              stmt.setDate(1, removalDate);
              stmt.setInt(2, user.getId());

              rows = stmt.executeUpdate();
              if (rows > 0) {
                user.setRemovalDate(removalDate);
                Group group = user.getGroup();
                if (group != null && group.getId() != 0) {
                  if (selectAllByGroupId(group) == null) {
                    DaoFactory.getGroupDao().remove(group);
                  }
                }
              }
            } catch (SQLException e) {
              rows = switchSqlError(method, e);
            } finally {
              try {
                // free resources
                if (result != null) {
                  result.close();
                }
                if (stmt != null) {
                  stmt.close();
                }
                if (closeCon) {
                  MysqlConnector.close();
                }
              } catch (SQLException e) {
                logSqlError("closure " + method, e);
              }
            }
          }
        });

    try {
      thread.start();
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return rows;
  }

  private void reactivate() {
    // TODO LATER: someday implement method
  }

  public ArrayList<User> getUsers() {
    return users;
  }

  public void setUsers(ArrayList<User> users) {
    this.users = users;
  }

  public void setUser(User user) {
    this.user = user;
  }
}