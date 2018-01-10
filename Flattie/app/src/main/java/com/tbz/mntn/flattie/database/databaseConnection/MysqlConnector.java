package com.tbz.mntn.flattie.database.databaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * DO NOT PUSH THIS TO GITHUB!!!!
 */

public class MysqlConnector {
    private static Connection connection;

    public static Connection getConnection(){
        return connection;
    }

    public static Connection connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://moeby.ch:3306/moebych_Flattie?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "moebych_Flattie", "tE-3Ri;9yUa;2c");
        } catch (Exception e) {
            System.err.println(e);
        }
        return connection;
    }

    public static void close() {
        try {
            if(connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
