package com.tbz.mntn.flattie.databaseConnection;

import org.junit.Ignore;
import org.junit.Test;

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