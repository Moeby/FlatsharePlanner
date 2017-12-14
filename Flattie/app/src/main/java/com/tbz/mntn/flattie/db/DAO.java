package com.tbz.mntn.flattie.db;


public abstract class DAO {

    /**
     * change sqlCode in internal error codes for easier error handling
     * @param sqlCode
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
    public int switchSQLError(int sqlCode) {
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
}