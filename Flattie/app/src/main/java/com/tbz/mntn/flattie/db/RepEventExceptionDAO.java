package com.tbz.mntn.flattie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RepEventExceptionDAO {
    private static RepEventExceptionDAO instance = new RepEventExceptionDAO();
    private ArrayList<RepEventExeption> repEventExeptions = new ArrayList();

    // table constants
    private static final String TABLE = "rep_event_exeption";
    private static final String ID = "id";
    private static final String START = "start_datetime";
    private static final String END = "end_datetime";
    private static final String SKIPPED = "skipped";
    private static final String CALENDAR_ITEM_FK = "calendar_item_fk";


    private RepEventExceptionDAO(){}

    public static RepEventExceptionDAO getInstance() {
        return instance;
    }

    // TESTME: #44
    public int insert(RepEventExeption repEventExeption){
        // TODO: #44 get connection
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try{
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + START + "," + END + "," + SKIPPED + "," + CALENDAR_ITEM_FK + ")"
                            + " VALUES( ?, ?, ?, ?, ?);"
                    , Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, repEventExeption.getStart());
            stmt.setDate(2, repEventExeption.getEnd());
            stmt.setBoolean(3, repEventExeption.isSkipped());
            stmt.setInt(2, repEventExeption.getCalendarItem().getId());

            rows = stmt.executeUpdate();
            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    repEventExeption.setId(generatedKeys.getInt(1));
                }
            }

            if(rows > 0){
                repEventExeptions.add(repEventExeption);
            }

        } catch (SQLException e){
            // TODO: #44 implement errorhandling
        } finally {
            try  {
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

    public void selectByCalendarItem(){
        // TODO: #44 implement method
    }

    public void update(){
        // TODO: #44 implement method
    }

    // TESTME: #44
    public int delete(RepEventExeption repEventExeption){
        // TODO: #44 get connection
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try{
            stmt = con.prepareStatement("DELETE FROM " + TABLE
                                    + " WHERE " + ID + " = ?;"
                                , Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, repEventExeption.getId());

            rows = stmt.executeUpdate();
            if(rows > 0){
                repEventExeptions.remove(repEventExeption);
            }

        } catch (SQLException e){
            // TODO: #44 implement errorhandling
        } finally {
            try  {
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
