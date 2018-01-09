package com.tbz.mntn.flattie.database.dao;


import com.tbz.mntn.flattie.database.databaseConnection.MysqlConnector;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DAO {
    protected boolean closeCon;
    protected Connection con;

    protected Connection getConnection(String method) {

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

    /**
     * change sqlCode in internal error codes for easier error handling
     * log error message
     *
     * @param method
     * @param e
     * @return -999 for unknown / unhandled errors
     * -100 for not found
     * -200 for duplicates
     * -300 for foreign keys --> does your fk exist in db? is it possible to update it?
     * -400 for other locks --> try it later again
     * -500 for wrong values (typically nullable errors)
     * -600 for wrong SQLQueries (should not occur in production)
     */
    protected int switchSQLError(String method, SQLException e) {
        logSQLError(method, e);
        int sqlCode = e.getErrorCode();
        switch (sqlCode) {
            case 1062:
                return -200;
            case 1451:
            case 1452:
                return -300;
            case 1048:
                return -500;

            //1146 table doesnt exist
            /*
            case notFound:      return -100;
            case locks:         return -400;
            case SQLQuery:      return -600;
            */
            default:
                return -999;
        }
        /*
        1062 duplicate
        1022 duplicate
        1165 table locked
        */
    }

    protected void logSQLError(String method, SQLException e) {
        //todo: get Log.w back!
//        Log.w(TAG, method+": ", e);
        System.out.println(method + ": " + e);
        System.out.println(e.getErrorCode() + ", " + e.getSQLState());
    }

    public void setCloseCon(boolean closeCon) {
        this.closeCon = closeCon;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
}