package com.tbz.mntn.flattie.db;

import com.tbz.mntn.flattie.databaseConnection.MysqlConnector;

import java.sql.*;
import java.util.ArrayList;

public class GroupDAO extends DAO {
    private static GroupDAO instance    = new GroupDAO();
    private ArrayList<Group> groups     = new ArrayList();

    // table constants
    private static final String TABLE           = "moebych_Flattie.group";
    private static final String ID              = "id";
    private static final String NAME            = "name";
    private static final String REMOVAL_DATE    = "removal_date";

    private GroupDAO() {
    }

    public static GroupDAO getInstance() {
        return instance;
    }

    /**
     * @param group required values: name <br>possible nullable: removal_date
     * @return
     */
    public int insert(Group group) {
        String method = "insert " + TABLE;
        int rows                = -1;
        Connection con          = getConnection(method);
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + NAME + ", " + REMOVAL_DATE + ")"
                                        + " VALUES(?, ?);"
                                        , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,   group.getName());
            Date removalDate = group.getRemovalDate();
            if(removalDate != null)
                stmt.setDate(2,     removalDate);
            else
                stmt.setNull(2, Types.DATE);

            rows = stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next())
                group.setId(generatedKeys.getInt(1));

            if (rows > 0)
                groups.add(group);

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
     * @param id
     * @return group from database or null if not found / an error occurred <br>ignores removed groups
     */
    public Group selectById(int id) {
        String method = "selectById " + TABLE;
        Group group             = null;
        Connection con          = getConnection(method);
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("SELECT " + NAME + " FROM " + TABLE
                                        + " WHERE " + ID + " = ?"
                                        + " AND " + REMOVAL_DATE + " IS NULL;");
            stmt.setInt(1, id);
            result = stmt.executeQuery();
            if (result.next()) {
                for (Group savedGroup : groups) {
                    if (id == savedGroup.getId()) {
                        group = savedGroup;
                        break;
                    }
                }
                if (group == null) {
                    group = new Group();
                    groups.add(group);
                }
                group.setId(id);
                group.setName(result.getString(NAME));

                //testme: #44 do they callback? --> they do!
                //group.setUsers((ArrayList<User>) DAOFactory.getUserDAO().selectAllByGroupId(group));
                //group.setShoppingItems((ArrayList<ShoppingItem>) DAOFactory.getShoppingItemDAO().selectAllByGroupId(group));
                //group.setCalendarItems((ArrayList<CalendarItem>) DAOFactory.getCalendarItemDAO().selectAllByGroupId(group));
            }
        } catch (SQLException e) {
            logSQLError(method, e);
            group = null;
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
        return group;
    }

    /**
     * update removal_date to current date - this group will be ignored by future selects
     * @param group required values: id <br>ignored values: removal date
     * @return
     */
    public int remove(Group group) {
        String method = "remove " + TABLE;
        Date removalDate = new Date(new java.util.Date().getTime());
        int rows = -1;
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("UPDATE " + TABLE
                                        + " SET " + REMOVAL_DATE + " = ?"
                                        + " WHERE " + ID + " = ?;");
            stmt.setDate(1, removalDate);
            stmt.setInt(2,  group.getId());

            rows = stmt.executeUpdate();
            if (rows > 0) {
                group.setRemovalDate(removalDate);
            }

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

    private void reactivate(Group group) {
        // TODO: someday implement method
        // #44 at the moment no required feature!
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
}
