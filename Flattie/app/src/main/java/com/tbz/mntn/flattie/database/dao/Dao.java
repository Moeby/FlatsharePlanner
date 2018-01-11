package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.databaseConnection.MysqlConnector;

import java.sql.Connection;
import java.sql.SQLException;

abstract class Dao {
  boolean closeCon;
  private Connection con;

  Connection getConnection() {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            setCon(MysqlConnector.getConnection());
            setCloseCon(false);

            setCon(MysqlConnector.connect());
            setCloseCon(true);
          }
        });

    try {
      thread.start();
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return con;
  }

  Connection getConnection(final String method) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            con = MysqlConnector.getConnection();
            closeCon = false;
            try {
              if (con == null || con.isClosed()) {
                con = MysqlConnector.connect();
                closeCon = true;
              }
            } catch (SQLException e) {
              logSqlError(method, e);
            }
          }
        });

    try {
      thread.start();
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return con;
  }

  /**
   * Change sqlCode in internal error codes for easier error handling.
   * log error message
   * @param method name of method which throws the SQL error
   * @param e      throws
   * @return -999 for unknown / unhandled errors
   * -100 for not found (not supported yet)
   * -200 for duplicates
   * -300 for foreign keys --> does your fk exist in db? is it possible to update it?
   * -400 for other locks --> try it later again
   * -500 for wrong values (typically nullable errors)
   * -600 for wrong SQLQueries (should not occur in production) (not supported yet)
   */
  int switchSqlError(String method, SQLException e) {
    logSqlError(method, e);
    int sqlCode = e.getErrorCode();
    switch (sqlCode) {
      /*
      cases which did not occur yet
      case notFound:      return -100;
      case SQLQuery:      return -600;
      */
      case 1022:
      case 1062:
        return -200;
      case 1451:
      case 1452:
        return -300;
      case 1165: // table locked
        return -400;
      case 1048:
      case 1146: //table does not exist
        return -500;
      default:
        return -999;
    }
  }

  void logSqlError(String method, SQLException e) {
    //TODO: get Log.w back!
    //        Log.w(TAG, method+": ", e);
    System.out.println(method + ": " + e);
    System.out.println(e.getErrorCode() + ", " + e.getSQLState());
  }

  private void setCloseCon(boolean closeCon) {
    this.closeCon = closeCon;
  }

  private void setCon(Connection con) {
    this.con = con;
  }
}