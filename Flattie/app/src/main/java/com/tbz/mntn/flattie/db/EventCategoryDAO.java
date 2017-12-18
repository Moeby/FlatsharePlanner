package com.tbz.mntn.flattie.db;

import com.tbz.mntn.flattie.databaseConnection.MysqlConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventCategoryDAO extends DAO {
    private static EventCategoryDAO instance            = new EventCategoryDAO();
    private ArrayList<EventCategory> eventCategories    = new ArrayList();

    // table constants
    private static final String TABLE   = "event_category";
    private static final String ID      = "id";
    private static final String NAME    = "name";

    private EventCategoryDAO() {
    }

    public static EventCategoryDAO getInstance() {
        return instance;
    }

    // TESTME: #44
    // return null if not found
    public EventCategory selectById(int id) {
        String method = "selectById " + TABLE;
        EventCategory category  = null;
        Connection con          = getConnection(method);
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("SELECT " + NAME + " FROM " + TABLE
                                        + " WHERE " + ID + " = ?;");
            stmt.setInt(1, id);

            result = stmt.executeQuery();
            if (result.next()) {
                for (EventCategory eventCategory : eventCategories) {
                    if (id == eventCategory.getId()) {
                        category = eventCategory;
                        break;
                    }
                }
                if (category == null) {
                    category = new EventCategory();
                    eventCategories.add(category);
                }
                category.setId(id);
                category.setName(result.getString(NAME));
            }
        } catch (SQLException e) {         
            logSQLError(method, e);
            category = null;
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
        return category;
    }

    // TESTME: #44
    // return null if not found
    public List<EventCategory> selectAll() {
        String method = "selectAll " + TABLE;
        List<EventCategory> categories  = new ArrayList();
        Connection con                  = getConnection(method);
        PreparedStatement stmt          = null;
        ResultSet result                = null;
        try {
            stmt    = con.prepareStatement("SELECT " + ID + ", " + NAME + " FROM " + TABLE + ";");
            result  = stmt.executeQuery();
            while (result.next()) {
                int id                  = result.getInt(ID);
                EventCategory category  = null;
                for (EventCategory eventCategory : eventCategories) {
                    if (id == eventCategory.getId()) {
                        category = eventCategory;
                        break;
                    }
                }
                if (category == null) {
                    category = new EventCategory();
                    eventCategories.add(category);
                }
                category.setId(id);
                category.setName(result.getString(NAME));

                categories.add(category);
            }
        } catch (SQLException e) {
            logSQLError(method, e);
            categories = null;
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
        if (!categories.isEmpty()) {
            return categories;
        } else {
            return null;
        }
    }

    public ArrayList<EventCategory> getEventCategories() {
        return eventCategories;
    }

    public void setEventCategories(ArrayList<EventCategory> eventCategories) {
        this.eventCategories = eventCategories;
    }
}
