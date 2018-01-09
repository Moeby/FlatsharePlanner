package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.databaseConnection.MysqlConnector;
import com.tbz.mntn.flattie.database.dataclasses.CalendarItem;
import com.tbz.mntn.flattie.database.dataclasses.EventCategory;
import com.tbz.mntn.flattie.database.dataclasses.Group;
import com.tbz.mntn.flattie.database.dataclasses.RepEventException;
import com.tbz.mntn.flattie.database.dataclasses.Repeatable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CalendarItemDAO extends DAO {
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

    private CalendarItemDAO() {
    }

    public static CalendarItemDAO getInstance() {
        return instance;
    }

    /**
     * @param calendarItem required values: description, repeatable, start, end, group, eventCategory with id
     * @return
     */
    public int insert(CalendarItem calendarItem) {
        String method = "insert " + TABLE;
        int rows = -1;
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + DESCRIPTION + "," + REPEATABLE + "," + START + "," + END + "," + GROUP_FK + "," + EVENT_CATEGORY_FK + ")"
                            + " VALUES( ?, ?, ?, ?, ?, ?);"
                    , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, calendarItem.getDescription());
            stmt.setString(2, calendarItem.getRepeatable().toString());
            stmt.setDate(3, calendarItem.getStartDatetime());
            stmt.setDate(4, calendarItem.getEndDatetime());
            stmt.setInt(5, calendarItem.getGroup().getId());
            EventCategory category = calendarItem.getEventCategory();
            if (category != null && category.getId() != 0)
                stmt.setInt(6, calendarItem.getEventCategory().getId());

            rows = stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next())
                calendarItem.setId(generatedKeys.getInt(1));

            if (rows > 0)
                calendarItems.add(calendarItem);
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

    /**
     * @param id
     * @return calendar item from database or null if not found / an error occurred
     */
    public CalendarItem selectById(int id) {
        String method = "selectById " + TABLE;
        CalendarItem item = null;
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("SELECT " + DESCRIPTION + "," + REPEATABLE + "," + START + "," + END + "," + EVENT_CATEGORY_FK + "," + GROUP_FK + " FROM " + TABLE
                    + " WHERE " + ID + " = ?;");
            stmt.setInt(1, id);

            result = stmt.executeQuery();
            if (result.next()) {
                for (CalendarItem calendarItem : calendarItems) {
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
                // FIXME: handle timestamp / date / whatever!
                //item.setStartDatetime(result.getDate(START));
                //item.setEndDatetime(result.getDate(END));

                item.setRepEventExceptions((ArrayList<RepEventException>) DAOFactory.getRepEventExeptionDAO().selectAllByCalendarItem(item));

                item.setEventCategory(DAOFactory.getEventCategoryDAO().selectById(result.getInt(EVENT_CATEGORY_FK), item));

                item.setGroup(DAOFactory.getGroupDAO().selectById(result.getInt(GROUP_FK)));

            }
        } catch (SQLException e) {
            logSQLError(method, e);
            MysqlConnector.close();
            item = null;
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
        return item;
    }

    /**
     * @param group
     * @return calendar items from database or null if not found / an error occurred
     */
    public List<CalendarItem> selectAllByGroupId(Group group) {
        return selectAllByGroupId(group, null);
    }

    // TESTME: #44

    /**
     * @param group
     * @return calendar items from database or null if not found / an error occurred
     */
    public List<CalendarItem> selectAllByGroupId(Group group, CalendarItem callerCalendarItem) {
        String method = "selectAllByGroupId" + TABLE;
        List<CalendarItem> itemList = new ArrayList();
        int groupFk = group.getId();
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("SELECT " + ID + "," + DESCRIPTION + "," + REPEATABLE + "," + START + "," + END + "," + EVENT_CATEGORY_FK + " FROM " + TABLE
                    + " WHERE " + GROUP_FK + " = ?;");
            stmt.setInt(1, groupFk);
            result = stmt.executeQuery();
            while (result.next()) {
                int id = result.getInt(ID);
                CalendarItem item = null;
                if (callerCalendarItem == null || id != callerCalendarItem.getId()) {
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
                    // FIXME: handle timestamp / date / whatever!
                    //item.setStartDatetime(result.getDate(START));
                    //item.setEndDatetime(result.getDate(END));

                    item.setRepEventExceptions((ArrayList<RepEventException>) DAOFactory.getRepEventExeptionDAO().selectAllByCalendarItem(item));
                    item.setGroup(group);
                    item.setEventCategory(DAOFactory.getEventCategoryDAO().selectById(result.getInt(EVENT_CATEGORY_FK), item));
                } else {
                    item = callerCalendarItem;
                    item.setGroup(group);
                }
                itemList.add(item);
            }

        } catch (SQLException e) {
            logSQLError(method, e);
            MysqlConnector.close();
            MysqlConnector.close();
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
        if (itemList != null && !itemList.isEmpty()) {
            return itemList;
        } else {
            return null;
        }
    }

    // TESTME: #44

    /**
     * @param category
     * @return calendar items from database or null if not found / an error occurred
     */
    public List<CalendarItem> selectAllByEventCategory(EventCategory category, CalendarItem callerCalendarItem) {
        String method = "selectAllByEventCategory" + TABLE;
        List<CalendarItem> itemList = new ArrayList();
        int categoryFK = category.getId();
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("SELECT " + ID + "," + DESCRIPTION + "," + REPEATABLE + "," + START + "," + END + "," + GROUP_FK + " FROM " + TABLE
                    + " WHERE " + EVENT_CATEGORY_FK + " = ?;");
            stmt.setInt(1, categoryFK);
            result = stmt.executeQuery();
            while (result.next()) {
                int id = result.getInt(ID);
                CalendarItem item = null;
                if (callerCalendarItem == null || id != callerCalendarItem.getId()) {
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
                    // FIXME: handle timestamp / date / whatever!
                    //item.setStartDatetime(result.getDate(START));
                    //item.setEndDatetime(result.getDate(END));

                    item.setRepEventExceptions((ArrayList<RepEventException>) DAOFactory.getRepEventExeptionDAO().selectAllByCalendarItem(item));
                    item.setEventCategory(category);

                    int groupId = result.getInt(GROUP_FK);
                    if(callerCalendarItem != null) {
                        Group callerGroup = callerCalendarItem.getGroup();
                        if (callerGroup == null || callerGroup.getId() != groupId) {
                            item.setGroup(DAOFactory.getGroupDAO().selectById(groupId, callerCalendarItem));
                        } else {
                            item.setGroup(callerGroup);
                        }
                    } else {
                        item.setGroup(DAOFactory.getGroupDAO().selectById(groupId));
                    }
                } else {
                    item = callerCalendarItem;
                }
                itemList.add(item);
            }

        } catch (SQLException e) {
            logSQLError(method, e);
            MysqlConnector.close();
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

    /**
     * Calendar item gets updated. <strong>All exceptions will get deleted by calling this method!</strong>
     *
     * @param calendarItem
     * @return
     */
    public int update(CalendarItem calendarItem) {
        String method = "update" + TABLE;
        int rows = -1;
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            RepEventExceptionDAO dao = DAOFactory.getRepEventExeptionDAO();
            int check = dao.deleteAllByCalendarItem(calendarItem);

            /*
            if(check xx) {
                maybe do something
            }
             */

            stmt = con.prepareStatement("UPDATE " + TABLE
                    + " SET " + DESCRIPTION + " = ?,"
                    + REPEATABLE + " = ?,"
                    + START + " = ?,"
                    + END + " = ?,"
                    + GROUP_FK + " = ?,"
                    + EVENT_CATEGORY_FK + " = ?"
                    + " WHERE " + ID + " = ?;");
            stmt.setString(1, calendarItem.getDescription());
            stmt.setString(2, calendarItem.getRepeatable().toString());
            stmt.setDate(3, calendarItem.getStartDatetime());
            stmt.setDate(4, calendarItem.getEndDatetime());
            stmt.setInt(5, calendarItem.getGroup().getId());
            stmt.setInt(6, calendarItem.getEventCategory().getId());
            stmt.setInt(7, calendarItem.getId());
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

    /**
     * Calendar item gets deleted. <strong>All exceptions will get deleted as well by calling this method!</strong>
     *
     * @param calendarItem
     * @return
     */
    public int delete(CalendarItem calendarItem) {
        String method = "delete " + TABLE;
        int rows = -1;
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            boolean repeatable = (calendarItem.getRepeatable() != Repeatable.NONE);
            if (repeatable) {
                RepEventExceptionDAO handleExceptions = DAOFactory.getRepEventExeptionDAO();
                ArrayList<RepEventException> repEventExceptions = calendarItem.getRepEventExceptions();
                if (repEventExceptions != null) {
                    for (RepEventException repEventException : repEventExceptions)
                        handleExceptions.delete(repEventException);
                }
            }

            stmt = con.prepareStatement("DELETE FROM " + TABLE
                            + " WHERE " + ID + " = ?;"
                    , Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, calendarItem.getId());

            rows = stmt.executeUpdate();
            if (rows > 0)
                calendarItems.remove(calendarItem);
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

    public ArrayList<CalendarItem> getCalendarItems() {
        return calendarItems;
    }

    public void setCalendarItems(ArrayList<CalendarItem> calendarItems) {
        this.calendarItems = calendarItems;
    }
}
