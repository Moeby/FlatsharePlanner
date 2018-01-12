package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.databaseConnection.MysqlConnector;
import com.tbz.mntn.flattie.database.dataclasses.CalendarItem;
import com.tbz.mntn.flattie.database.dataclasses.EventCategory;
import com.tbz.mntn.flattie.database.dataclasses.Group;
import com.tbz.mntn.flattie.database.dataclasses.RepEventException;
import com.tbz.mntn.flattie.database.dataclasses.Repeatable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CalendarItemDao extends Dao {
  private static CalendarItemDao         instance      = new CalendarItemDao();
  private        ArrayList<CalendarItem> calendarItems = new ArrayList();
  private ArrayList<CalendarItem> itemList;
  private CalendarItem            item;
  private int                     rows;

  // table constants
  private static final String TABLE             = "calendar_item";
  private static final String ID                = "id";
  private static final String DESCRIPTION       = "description";
  private static final String REPEATABLE        = "repeatable";
  private static final String START             = "start_datetime";
  private static final String END               = "end_datetime";
  private static final String GROUP_FK          = "group_fk";
  private static final String EVENT_CATEGORY_FK = "event_category_fk";

  private CalendarItemDao() {
  }

  static CalendarItemDao getInstance() {
    return instance;
  }

  /**
   * Inserts a CalendarItem into the database.
   * @param calendarItem required values: description, repeatable,
   *                     start, end, group, eventCategory with id
   * @return positive = ok, negative = error
   * @see Dao#switchSqlError(String method, SQLException e)
   */
  public int insert(final CalendarItem calendarItem) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "insert " + TABLE;
            rows = -1;
            Connection        con    = getConnection(method);
            PreparedStatement stmt   = null;
            ResultSet         result = null;
            try {
              stmt = con.prepareStatement("INSERT INTO " + TABLE
                                          + " (" + DESCRIPTION
                                          + "," + REPEATABLE
                                          + "," + START
                                          + "," + END
                                          + "," + GROUP_FK
                                          + "," + EVENT_CATEGORY_FK + ")"
                                          + " VALUES( ?, ?, ?, ?, ?, ?);",
                                          Statement.RETURN_GENERATED_KEYS);
              stmt.setString(1, calendarItem.getDescription());
              stmt.setString(2, calendarItem.getRepeatable().toString());
              stmt.setTimestamp(3, calendarItem.getStartDatetime());
              stmt.setTimestamp(4, calendarItem.getEndDatetime());
              stmt.setInt(5, calendarItem.getGroup().getId());
              EventCategory category = calendarItem.getEventCategory();
              if (category != null && category.getId() != 0) {
                stmt.setInt(6, calendarItem.getEventCategory().getId());
              }

              rows = stmt.executeUpdate();
              ResultSet generatedKeys = stmt.getGeneratedKeys();
              if (generatedKeys.next()) {
                calendarItem.setId(generatedKeys.getInt(1));
              }

              if (rows > 0) {
                calendarItems.add(calendarItem);
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

  /**
   * Select a CalendarItem by its id.
   * This includes a select of all associated RepEventExceptions, EventCategories and Groups.
   * TODO #44: check for assignee!!
   * @param id of CalendarItem
   * @return calendar item from database or null if not found / an error occurred
   */
  public CalendarItem selectById(final int id) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "selectById " + TABLE;
            item = null;
            Connection        con    = getConnection(method);
            PreparedStatement stmt   = null;
            ResultSet         result = null;
            try {
              stmt = con.prepareStatement("SELECT "
                                          + DESCRIPTION + ","
                                          + REPEATABLE + ","
                                          + START + ","
                                          + END + ","
                                          + EVENT_CATEGORY_FK + ","
                                          + GROUP_FK
                                          + " FROM " + TABLE
                                          + " WHERE " + ID + " = ?;");
              stmt.setInt(1, id);

              result = stmt.executeQuery();
              if (result.next()) {
                for (CalendarItem calendarItem : calendarItems) {
                  if (id == calendarItem.getId()) {
                    item = calendarItem;
                    break;
                  }
                }
                if (item == null) {
                  item = new CalendarItem();
                  calendarItems.add(item);
                }
                item.setId(id);
                item.setDescription(result.getString(DESCRIPTION));
                item.setRepeatable(Repeatable.toRepeatable(result.getString(REPEATABLE)));
                item.setStartDatetime(result.getTimestamp(START));
                item.setEndDatetime(result.getTimestamp(END));

                item.setRepEventExceptions(DaoFactory.getRepEventExeptionDao()
                                                     .selectAllByCalendarItem(item));
                item.setEventCategory(DaoFactory.getEventCategoryDao()
                                                .selectById(result.getInt(EVENT_CATEGORY_FK),
                                                            item));
                item.setGroup(DaoFactory.getGroupDao()
                                        .selectById(result.getInt(GROUP_FK)));
              }
            } catch (SQLException e) {
              logSqlError(method, e);
              MysqlConnector.close();
              item = null;
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
    return item;
  }

  /**
   * Get all events by current flattie group.
   * @param group current flattie group where the user is a member
   * @return calendar items from database or null if not found / an error occurred
   * @see #selectAllByGroupId(Group, CalendarItem)
   */
  public ArrayList<CalendarItem> selectAllByGroupId(Group group) {
    return selectAllByGroupId(group, null);
  }

  /**
   * Get all events by current flattie group.
   * @param group              current flattie group where the user is a member
   * @param callerCalendarItem null if called from outside
   *                           or event which has called another select method
   * @return calendar items from database or null if not found / an error occurred
   */
  ArrayList<CalendarItem> selectAllByGroupId(final Group group,
                                             final CalendarItem callerCalendarItem) {

    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "selectAllByGroupId" + TABLE;
            itemList = new ArrayList();
            int               groupFk = group.getId();
            Connection        con     = getConnection(method);
            PreparedStatement stmt    = null;
            ResultSet         result  = null;
            try {
              stmt = con.prepareStatement("SELECT "
                                          + ID + ","
                                          + DESCRIPTION + ","
                                          + REPEATABLE + ","
                                          + START + ","
                                          + END + ","
                                          + EVENT_CATEGORY_FK
                                          + " FROM " + TABLE
                                          + " WHERE " + GROUP_FK + " = ?;");
              stmt.setInt(1, groupFk);
              result = stmt.executeQuery();
              while (result.next()) {
                int          id   = result.getInt(ID);
                CalendarItem item = null;
                if (callerCalendarItem == null || id != callerCalendarItem.getId()) {
                  for (CalendarItem savedItem : calendarItems) {
                    if (id == savedItem.getId()) {
                      item = savedItem;
                      break;
                    }
                  }
                  if (item == null) {
                    item = new CalendarItem();
                    calendarItems.add(item);
                  }
                  item.setId(id);
                  item.setDescription(result.getString(DESCRIPTION));
                  item.setRepeatable(Repeatable.toRepeatable(result.getString(REPEATABLE)));
                  item.setStartDatetime(result.getTimestamp(START));
                  item.setEndDatetime(result.getTimestamp(END));

                  item.setRepEventExceptions(DaoFactory.getRepEventExeptionDao()
                                                       .selectAllByCalendarItem(item));
                  item.setGroup(group);
                  item.setEventCategory(DaoFactory.getEventCategoryDao()
                                                  .selectById(result.getInt(EVENT_CATEGORY_FK),
                                                              item));
                } else {
                  item = callerCalendarItem;
                  item.setGroup(group);
                }
                itemList.add(item);
              }

            } catch (SQLException e) {
              logSqlError(method, e);
              MysqlConnector.close();
              MysqlConnector.close();
              itemList = null;
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
    if (itemList != null && !itemList.isEmpty()) {
      return itemList;
    } else {
      return null;
    }
  }

  /**
   * Select all CalendarItems which has a specific category.
   * @param category search criteria
   * @return calendar items from database or null if not found / an error occurred
   * @see #selectAllByEventCategory(EventCategory, CalendarItem)
   */
  public ArrayList<CalendarItem> selectAllByEventCategory(EventCategory category) {
    return selectAllByEventCategory(category, null);
  }

  /**
   * Select all CalendarItems which has a specific category.
   * @param category           search criteria
   * @param callerCalendarItem null if called from outside
   *                           or event which has called another select method
   * @return calendar items from database or null if not found / an error occurred
   */
  ArrayList<CalendarItem> selectAllByEventCategory(final EventCategory category,
                                                   final CalendarItem callerCalendarItem) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "selectAllByEventCategory" + TABLE;
            itemList = new ArrayList();
            int               categoryFk = category.getId();
            Connection        con        = getConnection(method);
            PreparedStatement stmt       = null;
            ResultSet         result     = null;
            try {
              stmt = con.prepareStatement("SELECT "
                                          + ID + ","
                                          + DESCRIPTION + ","
                                          + REPEATABLE + ","
                                          + START + ","
                                          + END + ","
                                          + GROUP_FK
                                          + " FROM " + TABLE
                                          + " WHERE " + EVENT_CATEGORY_FK + " = ?;");
              stmt.setInt(1, categoryFk);
              result = stmt.executeQuery();
              while (result.next()) {
                int          id   = result.getInt(ID);
                CalendarItem item = null;
                if (callerCalendarItem == null || id != callerCalendarItem.getId()) {
                  for (CalendarItem savedItem : calendarItems) {
                    if (id == savedItem.getId()) {
                      item = savedItem;
                      break;
                    }
                  }
                  if (item == null) {
                    item = new CalendarItem();
                    calendarItems.add(item);
                  }
                  item.setId(id);
                  item.setDescription(result.getString(DESCRIPTION));
                  item.setRepeatable(Repeatable.toRepeatable(result.getString(REPEATABLE)));
                  item.setStartDatetime(result.getTimestamp(START));
                  item.setEndDatetime(result.getTimestamp(END));

                  item.setRepEventExceptions(DaoFactory.getRepEventExeptionDao()
                                                       .selectAllByCalendarItem(item));
                  item.setEventCategory(category);

                  int groupId = result.getInt(GROUP_FK);
                  if (callerCalendarItem != null) {
                    Group callerGroup = callerCalendarItem.getGroup();
                    if (callerGroup == null || callerGroup.getId() != groupId) {
                      item.setGroup(DaoFactory.getGroupDao()
                                              .selectById(groupId, callerCalendarItem));
                    } else {
                      item.setGroup(callerGroup);
                    }
                  } else {
                    item.setGroup(DaoFactory.getGroupDao().selectById(groupId));
                  }
                } else {
                  item = callerCalendarItem;
                }
                itemList.add(item);
              }

            } catch (SQLException e) {
              logSqlError(method, e);
              MysqlConnector.close();
              itemList = null;
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
    if (!itemList.isEmpty()) {
      return itemList;
    } else {
      return null;
    }
  }

  /**
   * Calendar item gets updated.
   * <strong>All exceptions will get deleted by calling this method!</strong>
   * @param calendarItem current event which should get updated
   * @return positive = ok, negative = error
   * @see Dao#switchSqlError(String method, SQLException e)
   */
  public int update(final CalendarItem calendarItem) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {

            String method = "update" + TABLE;
            rows = -1;
            Connection        con    = getConnection(method);
            PreparedStatement stmt   = null;
            ResultSet         result = null;
            try {
              RepEventExceptionDao dao   = DaoFactory.getRepEventExeptionDao();
              int                  check = dao.deleteAllByCalendarItem(calendarItem);

              /*
              Todo: when transaction inserted check
              if(check xx) {
                  maybe do something
              }
             */

              stmt = con.prepareStatement("UPDATE " + TABLE
                                          + " SET " + DESCRIPTION + " = ?,"
                                          + REPEATABLE + " = ?,"
                                          + START + " = ?,"
                                          + END + " = ?,"
                                          + GROUP_FK + " = ?,"
                                          + EVENT_CATEGORY_FK + " = ?"
                                          + " WHERE " + ID + " = ?;");
              stmt.setString(1, calendarItem.getDescription());
              stmt.setString(2, calendarItem.getRepeatable().toString());
              stmt.setTimestamp(3, calendarItem.getStartDatetime());
              stmt.setTimestamp(4, calendarItem.getEndDatetime());
              stmt.setInt(5, calendarItem.getGroup().getId());
              stmt.setInt(6, calendarItem.getEventCategory().getId());
              stmt.setInt(7, calendarItem.getId());
              rows = stmt.executeUpdate();

            /*
            if (rows > 0) {
                maybe do something
            }
            */

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
   * Calendar item gets deleted.
   * <strong>All exceptions will get deleted as well by calling this method!</strong>
   * @param calendarItem current event which gets deleted
   * @return positive = ok, negative = error
   * @see Dao#switchSqlError(String method, SQLException e)
   */
  public int delete(final CalendarItem calendarItem) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String            method = "delete " + TABLE;
            int               rows   = -1;
            Connection        con    = getConnection(method);
            PreparedStatement stmt   = null;
            ResultSet         result = null;
            try {
              boolean repeatable = (calendarItem.getRepeatable() != Repeatable.NONE);
              if (repeatable) {
                RepEventExceptionDao         exceptionDao    = DaoFactory.getRepEventExeptionDao();
                ArrayList<RepEventException> eventExceptions = calendarItem.getRepEventExceptions();
                if (eventExceptions != null) {
                  for (RepEventException repEventException : eventExceptions) {
                    exceptionDao.delete(repEventException);
                  }
                }
              }

              stmt = con.prepareStatement("DELETE FROM " + TABLE
                                          + " WHERE " + ID + " = ?;",
                                          Statement.RETURN_GENERATED_KEYS);
              stmt.setInt(1, calendarItem.getId());

              rows = stmt.executeUpdate();
              if (rows > 0) {
                calendarItems.remove(calendarItem);
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

  public ArrayList<CalendarItem> getCalendarItems() {
    return calendarItems;
  }

  public void setCalendarItems(ArrayList<CalendarItem> calendarItems) {
    this.calendarItems = calendarItems;
  }
}