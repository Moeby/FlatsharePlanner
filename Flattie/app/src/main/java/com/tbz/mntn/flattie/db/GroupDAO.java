package com.tbz.mntn.flattie.db;

import java.sql.*;
import java.util.ArrayList;

// TODO: #44 INSERT CONNECTION IN ALL METHODS
public class GroupDAO {
    private static GroupDAO instance    = new GroupDAO();
    private ArrayList<Group> groups     = new ArrayList();

    // table constants
    private static final String TABLE           = "group";
    private static final String ID              = "id";
    private static final String NAME            = "name";
    private static final String REMOVAL_DATE    = "removal_date";

    private GroupDAO() {
    }

    public static GroupDAO getInstance() {
        return instance;
    }

    // TESTME: #44
    public int insert(Group group) {
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + NAME + "," + REMOVAL_DATE + ")"
                                        + " VALUES( ?, ?);"
                                        , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, group.getName());
            stmt.setString(2, group.getRemovalDate());

            rows = stmt.executeUpdate();
            try {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next())
                    group.setId(generatedKeys.getInt(1));
            } catch (SQLException e){
                // TODO: #44 implement errorhandling
            }

            if (rows > 0)
                groups.add(group);

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

    // TESTME: #44
    // return null if not found
    public Group selectById(int id) {
        Group group             = null;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("SELECT " + NAME + " FROM " + TABLE
                                        + " WHERE " + ID + " = ?;");
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
                    group.setId(id);
                    group.setName(result.getString(NAME));

                    groups.add(group);
                }
            }
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
        return group;
    }

    // TESTME: #44
    public int remove(Group group) {
        Date removalDate = new Date(new java.util.Date().getTime());
        int rows = -1;
        Connection con = null;
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
                // TODO: #44 check if needed
                groups.remove(group);
                group.setRemovalDate(removalDate);
                groups.add(group);
            }

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

    public void reactivate(Group group) {
        // TODO: someday implement method
        // #44 at the moment no required feature!
    }
}
