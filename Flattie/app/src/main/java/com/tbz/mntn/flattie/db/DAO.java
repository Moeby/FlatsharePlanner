package com.tbz.mntn.flattie.db;


public abstract class DAO {
    public int switchSQLError(int sqlCode) {
        switch (sqlCode){

            default:    return -1;
        }
        /*
        switch (errorCode) {
            case 1022: rows = -2;
                case : rows = ;
                case : rows = ;
            default: rows = -1;
        }
        1062 duplicate
        1022 duplicate
        1165 table locked
        1452 referenced information --> key problem
        1406 value to long
        1054
        1064 wrong value
        */
    }
}
