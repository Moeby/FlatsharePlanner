package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.databaseConnection.MysqlConnector;
import com.tbz.mntn.flattie.database.dataclasses.CalendarItem;
import com.tbz.mntn.flattie.database.dataclasses.EventCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventCategoryDAO extends DAO {
  private static EventCategoryDAO instance = new EventCategoryDAO();
  private ArrayList<EventCategory> eventCategories = new ArrayList();
  private List<EventCategory> categories;
  private EventCategory category;
  private int rows;

  // table constants
  private static final String TABLE = "event_category";
  private static final String ID = "id";
  private static final String NAME = "name";

  private EventCategoryDAO() {
  }

  public static EventCategoryDAO getInstance() {
    return instance;
  }

  /**
   * calls selectById with calendarItem = default null
   *
   * @param id
   * @return event category from database or null if not found / an error occurred
   */
  public EventCategory selectById(int id) {
    return selectById(id, null);
  }

  /**
   * @param id
   * @param callerCalendarItem
   * @return event category from database or null if not found / an error occurred
   */
  public EventCategory selectById(final int id, final CalendarItem callerCalendarItem) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "selectById " + TABLE;
            category = null;
            Connection con = getConnection(method);
            PreparedStatement stmt = null;
            ResultSet result = null;
            try {
              stmt = con.prepareStatement("SELECT " + NAME + " FROM " + TABLE
                  + " WHERE " + ID + " = ?;");
              stmt.setInt(1, id);

              result = stmt.executeQuery();
              if (result.next()) {
                for (EventCategory eventCategory : eventCategories) {
                  if (id == eventCategory.getId()) {
                    category = eventCategory;
                    break;
                  }
                }
                if (category == null) {
                  category = new EventCategory();
                  eventCategories.add(category);
                }
                category.setId(id);
                category.setName(result.getString(NAME));

                category.setCalendarItems((ArrayList<CalendarItem>) DAOFactory.getCalendarItemDAO().selectAllByEventCategory(category, callerCalendarItem));
              }
            } catch (SQLException e) {
              logSQLError(method, e);
              MysqlConnector.close();
              category = null;
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
                logSQLError("closure " + method, e);
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
    return category;
  }

  /**
   * @return all event categories from database or null if an error occurred
   */
  public List<EventCategory> selectAll() {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "selectAll " + TABLE;
            categories = new ArrayList();
            Connection con = getConnection(method);
            PreparedStatement stmt = null;
            ResultSet result = null;
            try {
              stmt = con.prepareStatement("SELECT " + ID + ", " + NAME + " FROM " + TABLE + ";");
              result = stmt.executeQuery();
              while (result.next()) {
                int id = result.getInt(ID);
                EventCategory category = null;
                for (EventCategory eventCategory : eventCategories) {
                  if (id == eventCategory.getId()) {
                    category = eventCategory;
                    break;
                  }
                }
                if (category == null) {
                  category = new EventCategory();
                  eventCategories.add(category);
                }
                category.setId(id);
                category.setName(result.getString(NAME));

                category.setCalendarItems((ArrayList<CalendarItem>) DAOFactory.getCalendarItemDAO().selectAllByEventCategory(category, null));

                categories.add(category);
              }
            } catch (SQLException e) {
              logSQLError(method, e);
              MysqlConnector.close();
              categories = null;
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
                logSQLError("closure " + method, e);
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
    if (!categories.isEmpty()) {
      return categories;
    } else {
      return null;
    }
  }

  public ArrayList<EventCategory> getEventCategories() {
    return eventCategories;
  }

  public void setEventCategories(ArrayList<EventCategory> eventCategories) {
    this.eventCategories = eventCategories;
  }
}
