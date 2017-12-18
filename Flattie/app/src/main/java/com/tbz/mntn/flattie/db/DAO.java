package com.tbz.mntn.flattie.db;


import com.tbz.mntn.flattie.databaseConnection.MysqlConnector;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DAO {

    protected boolean closeCon;

    protected Connection getConnection(String method) {
        Connection con = MysqlConnector.getConnection();
        closeCon = false;
        try {
            if (con == null || con.isClosed()) {
                con = MysqlConnector.connect();
                closeCon = true;
            }
        } catch (SQLException e) {
            logSQLError(method, e);
        }
        return con;
    }

    /**
     * change sqlCode in internal error codes for easier error handling
     * log error message
     * @param method
     * @param e
     * @return -999 for unknown error
     * -100 for not found
     * -200 for duplicates
     * -300 for foreign key locks
     * -400 for other locks --> try it later again
     * -500 for wrong values
     * -501 for nullable errors
     * -502 for values to long
     * -600 for wrong SQLQueries (should not occur in production)
     */
    protected int switchSQLError(String method, SQLException e) {
        logSQLError(method, e);
        int sqlCode = e.getErrorCode();
        switch (sqlCode){
            /*
            case notFound:      return -100;
            case duplicate:     return -200;
            case foreignKeys:   return -300;
            case locks:         return -400;
            case wrongVal:      return -500;
            case nullable:      return -501;
            case toLong:        return -502;
            case SQLQuery:      return -600;
            */
            default:            return -999;
        }
        /*
        1062 duplicate
        1022 duplicate
        1165 table locked
        1452 referenced information --> key problem
        1406 value to long
        1054
        1064 wrong value
        */
    }

    protected void logSQLError(String method, SQLException e){
        //todo: get Log.w back!
//        Log.w(TAG, method+": ", e);
        System.out.println(method+": "+e);
    }
}