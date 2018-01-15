package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.databaseConnection.MysqlConnector;
import com.tbz.mntn.flattie.database.dataclasses.CalendarItem;
import com.tbz.mntn.flattie.database.dataclasses.RepEventException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RepEventExceptionDao extends Dao {
  private static RepEventExceptionDao         instance           = new RepEventExceptionDao();
  private        ArrayList<RepEventException> repEventExceptions = new ArrayList();
  private ArrayList<RepEventException> itemList;
  private int                          rows;

  // table constants
  private static final String TABLE            = "rep_event_exception";
  private static final String ID               = "id";
  private static final String START            = "start_datetime";
  private static final String END              = "end_datetime";
  private static final String SKIPPED          = "skipped";
  private static final String CALENDAR_ITEM_FK = "calendar_item_fk";

  private RepEventExceptionDao() {}

  static RepEventExceptionDao getInstance() {
    return instance;
  }

  /**
   * Add an exception to an event.
   * @param repEventException Required values: start datetime, end datetime,
   *                          skipped, calendar item with id
   * @return positive = ok, negative = error
   * @see Dao#switchSqlError(String method, SQLException e)
   */
  public int insert(final RepEventException repEventException) {
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
                                          + START + ","
                                          + END + ","
                                          + SKIPPED + ","
                                          + CALENDAR_ITEM_FK + ")"
                                          + " VALUES( ?, ?, ?, ?);",
                                          Statement.RETURN_GENERATED_KEYS);
              stmt.setTimestamp(1, repEventException.getStartDatetime());
              stmt.setTimestamp(2, repEventException.getEndDatetime());
              stmt.setBoolean(3, repEventException.isSkipped());
              stmt.setInt(4, repEventException.getCalendarItem().getId());

              rows = stmt.executeUpdate();
              ResultSet generatedKeys = stmt.getGeneratedKeys();
              if (generatedKeys.next()) {
                repEventException.setId(generatedKeys.getInt(1));
              }

              if (rows > 0) {
                repEventExceptions.add(repEventException);
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
   * Get all exceptions for an event.
   * @param calendarItem Required values: id
   * @return list of RepEventException from database or null if not found / an error occurred
   */
  ArrayList<RepEventException> selectAllByCalendarItem(final CalendarItem calendarItem) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "selectAllByCalendarItem" + TABLE;
            itemList = new ArrayList();
            int               calendarItemFk = calendarItem.getId();
            Connection        con            = getConnection(method);
            PreparedStatement stmt           = null;
            ResultSet         result         = null;
            try {
              stmt = con.prepareStatement("SELECT "
                                          + ID + ","
                                          + START + ","
                                          + END + ","
                                          + SKIPPED
                                          + " FROM " + TABLE
                                          + " WHERE " + CALENDAR_ITEM_FK + " = ?;");
              stmt.setInt(1, calendarItemFk);

              result = stmt.executeQuery();
              while (result.next()) {
                int               id        = result.getInt(ID);
                RepEventException exception = null;
                for (RepEventException savedException : repEventExceptions) {
                  if (id == savedException.getId()) {
                    exception = savedException;
                    break;
                  }
                }
                if (exception == null) {
                  exception = new RepEventException();
                  repEventExceptions.add(exception);
                }
                exception.setId(id);
                exception.setStartDatetime(result.getTimestamp(START));
                exception.setEndDatetime(result.getTimestamp(END));

                exception.setSkipped(result.getBoolean(SKIPPED));
                exception.setCalendarItem(calendarItem);

                itemList.add(exception);
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
   * Update current exception.
   * @param exception Required values: start datetime, end datetime, skipped<br>
   *                  Ignored values: CalendarItem
   * @return positive = ok, negative = error
   * @see Dao#switchSqlError(String method, SQLException e)
   */
  public int update(final RepEventException exception) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "update " + TABLE;
            rows = -1;
            Connection        con    = getConnection(method);
            PreparedStatement stmt   = null;
            ResultSet         result = null;
            try {
              stmt = con.prepareStatement("UPDATE " + TABLE + " SET "
                                          + START + " = ?,"
                                          + END + " = ?,"
                                          + SKIPPED + " = ?"
                                          + " WHERE " + ID + " = ?;");
              stmt.setTimestamp(1, exception.getStartDatetime());
              stmt.setTimestamp(2, exception.getEndDatetime());
              stmt.setBoolean(3, exception.isSkipped());
              stmt.setInt(4, exception.getId());

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
   * Delete a specific exception.
   * @param repEventException current exception which gets deleted
   * @return positive = ok, negative = error
   * @see Dao#switchSqlError(String method, SQLException e)
   */
  public int delete(final RepEventException repEventException) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "delete" + TABLE;
            rows = -1;
            Connection        con    = getConnection(method);
            PreparedStatement stmt   = null;
            ResultSet         result = null;
            try {
              stmt = con.prepareStatement("DELETE FROM " + TABLE
                                          + " WHERE " + ID + " = ?;",
                                          Statement.RETURN_GENERATED_KEYS);
              stmt.setInt(1, repEventException.getId());

              rows = stmt.executeUpdate();
              if (rows > 0) {
                repEventExceptions.remove(repEventException);
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
   * Delete all exceptions for one event.
   * @param calendarItem Required values: id
   * @return positive = ok, count deleted items<br>negative = error
   * @see #delete(RepEventException)
   * @see Dao#switchSqlError(String method, SQLException e)
   */
  public int deleteAllByCalendarItem(CalendarItem calendarItem) {
    // TODO LATER: transaction
    int                          deleted    = 0;
    ArrayList<RepEventException> exceptions = selectAllByCalendarItem(calendarItem);
    if (exceptions != null) {
      for (RepEventException exception : exceptions) {
        int check = delete(exception);
        if (check > 0) {
          deleted += check;
        } else {
          deleted = check;
          // TODO LATER: errorhandling, rollback
          break;
        }
      }
    }
    return deleted;
  }


  public ArrayList<RepEventException> getRepEventExceptions() {
    return repEventExceptions;
  }

  public void setRepEventExceptions(ArrayList<RepEventException> repEventExceptions) {
    this.repEventExceptions = repEventExceptions;
  }
}
