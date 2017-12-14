package com.tbz.mntn.flattie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// TODO: #44 INSERT CONNECTION IN ALL METHODS
public class CalendarItemDAO extends DAO {
    private static CalendarItemDAO instance         = new CalendarItemDAO();
    private ArrayList<CalendarItem> calendarItems   = new ArrayList();

    // table constants
    private static final String TABLE               = "calendar_item";
    private static final String ID                  = "id";
    private static final String DESCRIPTION         = "description";
    private static final String REPEATABLE          = "repeatable";
    private static final String START               = "start_datetime";
    private static final String END                 = "end_datetime";
    private static final String GROUP_FK            = "group_fk";
    private static final String EVENT_CATEGORY_FK   = "event_category_fk";

    private CalendarItemDAO() {
    }

    public static CalendarItemDAO getInstance() {
        return instance;
    }

    // TESTME: #44
    public int insert(CalendarItem calendarItem) {
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + DESCRIPTION + "," + REPEATABLE + "," + START + "," + END + "," + GROUP_FK + "," + EVENT_CATEGORY_FK + ")"
                                        + " VALUES( ?, ?, ?, ?, ?);"
                                        , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,   calendarItem.getDescription());
            stmt.setString(2,   calendarItem.getRepeatable().toString());
            stmt.setDate(3,     calendarItem.getStartDatetime());
            stmt.setDate(4,     calendarItem.getEndDatetime());
            stmt.setInt(5,      calendarItem.getGroup().getId());
            stmt.setInt(6,      calendarItem.getEventCategory().getId());

            rows = stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next())
                calendarItem.setId(generatedKeys.getInt(1));

            if (rows > 0)
                calendarItems.add(calendarItem);
        } catch (SQLException e) {
            rows = switchSQLError("insert CalendarItem", e);
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                logSQLError("closure insert CalendarItem", e);
            }
        }
        return rows;
    }

    // TESTME: #44
    // return null if not found
    public CalendarItem selectById(int id) {
        CalendarItem item       = null;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("SELECT " + DESCRIPTION + "," + REPEATABLE + "," + START + "," + END + "," + EVENT_CATEGORY_FK + "," + GROUP_FK + " FROM " + TABLE
                    + " WHERE " + ID + " = ?;");
            stmt.setInt(1, id);

            result = stmt.executeQuery();
            if (result.next()) {
                for (CalendarItem calendarItem: calendarItems) {
                    if (id == calendarItem.getId()) {
                        item = calendarItem;
                        break;
                    }
                }
                if (item == null) {
                    item = new CalendarItem();
                    calendarItems.add(item);
                }
                item.setId(id);
                item.setDescription(result.getString(DESCRIPTION));
                item.setRepeatable(Repeatable.toRepeatable(result.getString(REPEATABLE)));
                item.setStartDatetime(result.getDate(START));
                item.setEndDatetime(result.getDate(END));

                EventCategoryDAO dao = DAOFactory.getEventCategoryDAO();
                item.setEventCategory(dao.selectById(result.getInt(EVENT_CATEGORY_FK)));

                item.setGroup(DAOFactory.getGroupDAO().selectById(result.getInt(GROUP_FK)));

            }
        } catch (SQLException e) {
            logSQLError("selectById CalendarItem", e);
            item = null;
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                logSQLError("closure selectById CalendarItem", e);
            }
        }
        return item;
    }

    // TESTME: #44
    // return null if not found
    public List<CalendarItem> selectAllByGroupId(Group group) {
        List<CalendarItem> itemList = new ArrayList();
        int groupFk                 = group.getId();
        Connection con              = null;
        PreparedStatement stmt      = null;
        ResultSet result            = null;
        try {
            stmt = con.prepareStatement("SELECT " + ID + "," + DESCRIPTION + "," + REPEATABLE + "," + START + "," + END + "," + EVENT_CATEGORY_FK + " FROM " + TABLE
                    + " WHERE " + GROUP_FK + " = ?;");
            stmt.setInt(1, groupFk);
            result = stmt.executeQuery();
            while (result.next()) {
                int id = result.getInt(ID);
                CalendarItem item = null;
                for (CalendarItem savedItem : calendarItems) {
                    if (id == savedItem.getId()) {
                        item = savedItem;
                        break;
                    }
                }
                if (item == null) {
                    item = new CalendarItem();
                    calendarItems.add(item);
                }
                item.setId(id);
                item.setDescription(result.getString(DESCRIPTION));
                item.setRepeatable(Repeatable.toRepeatable(result.getString(REPEATABLE)));
                item.setStartDatetime(result.getDate(START));
                item.setEndDatetime(result.getDate(END));

                EventCategoryDAO dao = DAOFactory.getEventCategoryDAO();
                item.setEventCategory(dao.selectById(result.getInt(EVENT_CATEGORY_FK)));

                item.setGroup(group);

                itemList.add(item);
            }

        } catch (SQLException e) {
            logSQLError("selectAllByGroupId CalendarItem", e);
            itemList = null;
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                logSQLError("closure selectAllByGroupId CalendarItem", e);
            }
        }
        if (!itemList.isEmpty()) {
            return itemList;
        } else {
            return null;
        }
    }

    // TESTME: #44
    // exceptions get deleted by calling this method
    public int update(CalendarItem calendarItem) {
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            RepEventExceptionDAO dao = DAOFactory.getRepEventExeptionDAO();
            int check = dao.deleteAllByCalendarItem(calendarItem);

            /*
            if(check xx) {
                maybe do something
            }
             */

            stmt = con.prepareStatement("UPDATE " + TABLE
                    + " SET "   + DESCRIPTION       + " = ?,"
                                + REPEATABLE        + " = ?,"
                                + START             + " = ?,"
                                + END               + " = ?,"
                                + GROUP_FK          + " = ?,"
                                + EVENT_CATEGORY_FK + " = ?"
                    + " WHERE " + ID + " = ?;");
            stmt.setString(1,   calendarItem.getDescription());
            stmt.setString(2,   calendarItem.getRepeatable().toString());
            stmt.setDate(3,     calendarItem.getStartDatetime());
            stmt.setDate(4,     calendarItem.getEndDatetime());
            stmt.setInt(5,      calendarItem.getGroup().getId());
            stmt.setInt(6,      calendarItem.getEventCategory().getId());

            rows = stmt.executeUpdate();

            /*
            if (rows > 0) {
                maybe do something
            }
            */
        } catch (SQLException e) {
            rows = switchSQLError("update CalendarItem", e);
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                logSQLError("closure update CalendarItem", e);
            }
        }
        return rows;
    }

    // TESTME: #44
    public int delete(CalendarItem calendarItem) {
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            boolean repeatable  = (calendarItem.getRepeatable() != Repeatable.NONE);
            if (repeatable) {
                // TOREMEMBER: ask first if the user wants to delete the whole repeatable event
                RepEventExceptionDAO handleExceptions           = DAOFactory.getRepEventExeptionDAO();
                ArrayList<RepEventException> repEventExceptions  = calendarItem.getRepEventExceptions();
                for (RepEventException repEventException : repEventExceptions)
                    handleExceptions.delete(repEventException);
            }

            stmt = con.prepareStatement("DELETE FROM " + TABLE
                                        + " WHERE " + ID + " = ?;"
                                        , Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, calendarItem.getId());

            rows = stmt.executeUpdate();
            if (rows > 0)
                calendarItems.remove(calendarItem);
        } catch (SQLException e) {
            rows = switchSQLError("delete CalendarItem", e);
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                logSQLError("closure delete CalendarItem", e);
            }
        }
        return rows;
    }

    public ArrayList<CalendarItem> getCalendarItems() {
        return calendarItems;
    }

    public void setCalendarItems(ArrayList<CalendarItem> calendarItems) {
        this.calendarItems = calendarItems;
    }
}
