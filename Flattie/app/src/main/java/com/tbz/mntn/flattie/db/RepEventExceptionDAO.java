package com.tbz.mntn.flattie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// TODO: #44 INSERT CONNECTION IN ALL METHODS
public class RepEventExceptionDAO {
    private static RepEventExceptionDAO instance            = new RepEventExceptionDAO();
    private ArrayList<RepEventException> repEventExceptions = new ArrayList();

    // table constants
    private static final String TABLE               = "rep_event_exeption";
    private static final String ID                  = "id";
    private static final String START               = "start_datetime";
    private static final String END                 = "end_datetime";
    private static final String SKIPPED             = "skipped";
    private static final String CALENDAR_ITEM_FK    = "calendar_item_fk";


    private RepEventExceptionDAO() {
    }

    public static RepEventExceptionDAO getInstance() {
        return instance;
    }

    // TESTME: #44
    public int insert(RepEventException repEventException) {
        int rows = -1;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + START + "," + END + "," + SKIPPED + "," + CALENDAR_ITEM_FK + ")"
                                        + " VALUES( ?, ?, ?, ?, ?);"
                                        , Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1,     repEventException.getStart());
            stmt.setDate(2,     repEventException.getEnd());
            stmt.setBoolean(3,  repEventException.isSkipped());
            stmt.setInt(2,      repEventException.getCalendarItem().getId());

            rows = stmt.executeUpdate();
            try {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next())
                    repEventException.setId(generatedKeys.getInt(1));
            } catch (SQLException e){
                // TODO: #44 implement errorhandling
            }

            if (rows > 0)
                repEventExceptions.add(repEventException);

        } catch (SQLException e) {
            // TODO: #44 implement errorhandling
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                // TODO: #44 implement errorhandling
                System.out.println("Statement or result close failed");
            }
        }
        return rows;
    }

    public void selectByCalendarItem() {
        // TODO: #44 implement method
    }

    public void update() {
        // TODO: #44 implement method
    }

    // TESTME: #44
    public int delete(RepEventException repEventException) {
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("DELETE FROM " + TABLE
                                        + " WHERE " + ID + " = ?;"
                                        , Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, repEventException.getId());

            rows = stmt.executeUpdate();
            if (rows > 0)
                repEventExceptions.remove(repEventException);

        } catch (SQLException e) {
            // TODO: #44 implement errorhandling
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                // TODO: #44 implement errorhandling
                System.out.println("Statement or result close failed");
            }
        }
        return rows;
    }
}
