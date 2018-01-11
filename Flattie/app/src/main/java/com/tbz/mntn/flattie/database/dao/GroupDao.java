package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.databaseConnection.MysqlConnector;
import com.tbz.mntn.flattie.database.dataclasses.CalendarItem;
import com.tbz.mntn.flattie.database.dataclasses.Group;
import com.tbz.mntn.flattie.database.dataclasses.ShoppingItem;
import com.tbz.mntn.flattie.database.dataclasses.User;

import java.sql.*;
import java.util.ArrayList;

public class GroupDao extends Dao {
  private static GroupDao         instance = new GroupDao();
  private        ArrayList<Group> groups   = new ArrayList();
  private Group group;
  private int rows;

  // table constants
  private static final String TABLE = "moebych_Flattie.group";
  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String REMOVAL_DATE = "removal_date";

  private GroupDao() {
  }

  public static GroupDao getInstance() {
    return instance;
  }

  /**
   * @param group required values: name <br>possible nullable: removal_date
   * @return
   */
  public int insert(final Group group) {

    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "insert " + TABLE;
            rows = -1;
            Connection con = getConnection(method);
            PreparedStatement stmt = null;
            ResultSet result = null;
            try {
              stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + NAME + ", " + REMOVAL_DATE + ")"
                      + " VALUES(?, ?);"
                  , Statement.RETURN_GENERATED_KEYS);
              stmt.setString(1, group.getName());
              Date removalDate = group.getRemovalDate();
              if (removalDate != null)
                stmt.setDate(2, removalDate);
              else
                stmt.setNull(2, Types.DATE);

              rows = stmt.executeUpdate();
              ResultSet generatedKeys = stmt.getGeneratedKeys();
              if (generatedKeys.next())
                group.setId(generatedKeys.getInt(1));

              if (rows > 0)
                groups.add(group);

            } catch (SQLException e) {
              rows = switchSqlError(method, e);
            } finally {
              try {
                // free resources
                if (result != null)
                  result.close();
                if (stmt != null)
                  stmt.close();
                if (closeCon)
                  MysqlConnector.close();
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
   * calls selectById with calendarItem = default null
   *
   * @param id
   * @return group from database or null if not found / an error occurred <br>ignores removed groups
   */
  public Group selectById(int id) {
    return selectById(id, null);
  }

  /**
   * @param id
   * @param callerCalendarItem
   * @return group from database or null if not found / an error occurred <br>ignores removed groups
   */
  public Group selectById(final int id, final CalendarItem callerCalendarItem) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "selectById " + TABLE;
            group = null;
            Connection con = getConnection(method);
            PreparedStatement stmt = null;
            ResultSet result = null;
            try {
              stmt = con.prepareStatement("SELECT " + NAME + " FROM " + TABLE
                  + " WHERE " + ID + " = ?"
                  + " AND " + REMOVAL_DATE + " IS NULL;");
              stmt.setInt(1, id);
              result = stmt.executeQuery();
              if (result.next()) {
                for (Group savedGroup : groups) {
                  if (id == savedGroup.getId()) {
                    group = savedGroup;
                    break;
                  }
                }
                if (group == null) {
                  group = new Group();
                  groups.add(group);
                }
                group.setId(id);
                group.setName(result.getString(NAME));

                group.setUsers((ArrayList<User>) DaoFactory.getUserDao().selectAllByGroupId(group));
                group.setShoppingItems((ArrayList<ShoppingItem>) DaoFactory.getShoppingItemDao().selectAllByGroupId(group));
                group.setCalendarItems((ArrayList<CalendarItem>) DaoFactory.getCalendarItemDao().selectAllByGroupId(group, callerCalendarItem));
              }
            } catch (SQLException e) {
              logSqlError(method, e);
              MysqlConnector.close();
              group = null;
            } finally {
              try {
                // free resources
                if (result != null)
                  result.close();
                if (stmt != null)
                  stmt.close();
                if (closeCon)
                  MysqlConnector.close();
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
    return group;
  }

  /**
   * update removal_date to current date - this group will be ignored by future selects
   *
   * @param group required values: id <br>ignored values: removal date
   * @return
   */
  public int remove(final Group group) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "remove " + TABLE;
            Date removalDate = new Date(new java.util.Date().getTime());
            rows = -1;
            Connection con = getConnection(method);
            PreparedStatement stmt = null;
            ResultSet result = null;
            try {
              stmt = con.prepareStatement("UPDATE " + TABLE
                  + " SET " + REMOVAL_DATE + " = ?"
                  + " WHERE " + ID + " = ?;");
              stmt.setDate(1, removalDate);
              stmt.setInt(2, group.getId());

              rows = stmt.executeUpdate();
              if (rows > 0) {
                group.setRemovalDate(removalDate);
              }

            } catch (SQLException e) {
              rows = switchSqlError(method, e);
            } finally {
              try {
                // free resources
                if (result != null)
                  result.close();
                if (stmt != null)
                  stmt.close();
                if (closeCon)
                  MysqlConnector.close();
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

  private void reactivate(Group group) {
    // TODO LATER: someday implement method
    // #44 at the moment no required feature!
  }

  public ArrayList<Group> getGroups() {
    return groups;
  }

  public void setGroups(ArrayList<Group> groups) {
    this.groups = groups;
  }
}
