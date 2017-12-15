package com.tbz.mntn.flattie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// TODO: #44 INSERT CONNECTION IN ALL METHODS
public class RepEventExceptionDAO extends DAO {
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
        String method = "insert " + TABLE;
        int rows = -1;
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + START + "," + END + "," + SKIPPED + "," + CALENDAR_ITEM_FK + ")"
                                        + " VALUES( ?, ?, ?, ?);"
                                        , Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1,     repEventException.getStartDatetime());
            stmt.setDate(2,     repEventException.getEndDatetime());
            stmt.setBoolean(3,  repEventException.isSkipped());
            stmt.setInt(4,      repEventException.getCalendarItem().getId());

            rows = stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next())
                repEventException.setId(generatedKeys.getInt(1));

            if (rows > 0)
                repEventExceptions.add(repEventException);

        } catch (SQLException e) {
            rows = switchSQLError(method, e);
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
                if (closeCon)
                    MysqlConnector.close();
            } catch (SQLException e) {
                logSQLError("closure " + method, e);
            }
        }
        return rows;
    }

    // TESTME: #44
    // return null if not found
    public List<RepEventException> selectAllByCalendarItem(CalendarItem calendarItem) {
        String method = "selectAllByCalendarItem" + TABLE;
        List<RepEventException> itemList    = new ArrayList();
        int calendarItemFk                  = calendarItem.getId();
        Connection con                      = getConnection(method);
        PreparedStatement stmt              = null;
        ResultSet result                    = null;
        try {
            stmt = con.prepareStatement("SELECT " + ID + "," + START + "," + END + "," + SKIPPED + " FROM " + TABLE
                    + " WHERE " + CALENDAR_ITEM_FK + " = ?;");
            stmt.setInt(1, calendarItemFk);

            result = stmt.executeQuery();
            while (result.next()) {
                int id = result.getInt(ID);
                RepEventException exception = null;
                for (RepEventException savedException : repEventExceptions) {
                    if (id == savedException.getId()) {
                        exception = savedException;
                        break;
                    }
                }
                if (exception == null) {
                    exception = new RepEventException();
                    repEventExceptions.add(exception);
                }
                exception.setId(id);
                exception.setStartDatetime(result.getDate(START));
                exception.setEndDatetime(result.getDate(END));

                exception.setSkipped(result.getBoolean(SKIPPED));
                exception.setCalendarItem(calendarItem);

                itemList.add(exception);
            }

        } catch (SQLException e) {
            logSQLError(method, e);
            itemList = null;
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
                if (closeCon)
                    MysqlConnector.close();
            } catch (SQLException e) {
                logSQLError("closure " + method, e);
            }
        }
        if (!itemList.isEmpty()) {
            return itemList;
        } else {
            return null;
        }
    }

    // TESTME: #44
    public int update(RepEventException exception) {
        String method = "update " + TABLE;
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {

            stmt = con.prepareStatement("UPDATE " + TABLE
                    + " SET "   + START             + " = ?,"
                                + END               + " = ?,"
                                + SKIPPED           + " = ?,"
                                + CALENDAR_ITEM_FK  + " = ?"
                    + " WHERE " + ID + " = ?;");
            stmt.setDate(1,     exception.getStartDatetime());
            stmt.setDate(2,     exception.getEndDatetime());
            stmt.setBoolean(3,  exception.isSkipped());
            stmt.setInt(4,      exception.getCalendarItem().getId());

            rows = stmt.executeUpdate();

            /*
            if (rows > 0) {
                maybe do something
            }
            */
        } catch (SQLException e) {
            rows = switchSQLError(method, e);
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
                if (closeCon)
                    MysqlConnector.close();
            } catch (SQLException e) {
                logSQLError("closure " + method, e);
            }
        }
        return rows;
    }

    // TESTME: #44
    public int delete(RepEventException repEventException) {
        String method = "delete" + TABLE;
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
            rows = switchSQLError(method, e);
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
                if (closeCon)
                    MysqlConnector.close();
            } catch (SQLException e) {
                logSQLError("closure " + method, e);
            }
        }
        return rows;
    }

    // TESTME: #44
    public int deleteAllByCalendarItem(CalendarItem calendarItem) {
        int deleted = -1;
        List<RepEventException> exceptions = selectAllByCalendarItem(calendarItem);
        for(RepEventException exception: exceptions){
            int check = delete(exception);
            if(check > 0){
                deleted += check;
            }
            // todo: errorhandling
        }

        return deleted;
    }


    public ArrayList<RepEventException> getRepEventExceptions() {
        return repEventExceptions;
    }

    public void setRepEventExceptions(ArrayList<RepEventException> repEventExceptions) {
        this.repEventExceptions = repEventExceptions;
    }
}
