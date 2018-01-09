package com.tbz.mntn.flattie.database.databaseConnection;
<<<<<<< HEAD:Flattie/app/src/test/java/com/tbz/mntn/flattie/database/databaseConnection/MysqlConnectorTest.java
=======

import org.junit.Ignore;
import org.junit.Test;
>>>>>>> 8594c53073daf5c08c66458c7e264a23b6bd76b1:Flattie/app/src/test/java/com/tbz/mntn/flattie/database/databaseConnection/MysqlConnectorTest.java

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by tatyana on 12/10/2017.
 */
public class MysqlConnectorTest {

    @Ignore
    @Test
    public void connect(){
        try {
            MysqlConnector connector = new MysqlConnector();
            Connection con = connector.connect();
            assertTrue(con.isClosed());
            connector.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}