package com.tbz.mntn.flattie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// TODO: #44 INSERT CONNECTION IN ALL METHODS
public class CalendarItemDAO {
    private static CalendarItemDAO instance = new CalendarItemDAO();
    private ArrayList<CalendarItem> calendarItems = new ArrayList();

    // table constants
    private static final String TABLE = "calendar_item";
    private static final String ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String REPEATABLE = "repeatable";
    private static final String START = "start_datetime";
    private static final String END = "end_datetime";
    private static final String GROUP_FK = "group_fk";
    private static final String EVENT_CATEGORY_FK = "event_category_fk";

    private CalendarItemDAO(){}

    public static CalendarItemDAO getInstance() {
        return instance;
    }

    public void insert(){
        // TODO: #44 implement method
        // don't insert a group_fk first, maybe change attribute in DB to nullable
        // removal_date = null
    }

    // TESTME: #44
    public int insert(CalendarItem calendarItem){
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try{
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + DESCRIPTION + "," + REPEATABLE + "," + START + "," + END + "," + GROUP_FK + "," + EVENT_CATEGORY_FK + ")"
                            + " VALUES( ?, ?, ?, ?, ?);"
                    , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, calendarItem.getDescription());
            stmt.setString(2, calendarItem.getRepeatable());
            stmt.setDate(3, calendarItem.getStart());
            stmt.setDate(4, calendarItem.getEnd());
            stmt.setInt(5, calendarItem.getGroup().getId());
            stmt.setInt(6, calendarItem.getEventCategory().getId());

            rows = stmt.executeUpdate();
            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    calendarItem.setId(generatedKeys.getInt(1));
                }
            }

            if(rows > 0){
                calendarItems.add(calendarItem);
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

    public void selectAllByGroupId() {
        // TODO: #44 implement method
        // select by foreign key group_fk
    }

    public void update(){
        // TODO: #44 implement method
        // what do with exceptions?
    }

    // TESTME: #44
    public int delete(CalendarItem calendarItem){
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try{
            boolean repeatable = calendarItem.isRepeatable();
            if(repeatable){
                // TOREMEMBER: ask first if the user wants to delete the whole repeatable event
                RepEventExceptionDAO handleExceptions = DAOFactory.getRepEventExeptionDAO();
                ArrayList<RepEventExeption> repEventExceptions = calendarItem.getListOfExeptions();
                for(RepEventException repEventException: repEventExceptions){
                    handleExceptions.delete(repEventException);
                }
            }

            stmt = con.prepareStatement("DELETE FROM " + TABLE
                            + " WHERE " + ID + " = ?;"
                    , Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, calendarItem.getId());

            rows = stmt.executeUpdate();
            if(rows > 0){
                calendarItems.remove(calendarItem);
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
