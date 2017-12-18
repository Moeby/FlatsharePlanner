package com.tbz.mntn.flattie.db;

import com.tbz.mntn.flattie.databaseConnection.MysqlConnector;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DAO {
    private static UserDAO instance = new UserDAO();
    private ArrayList<User> users   = new ArrayList();

    // table constants
    private static final String TABLE           = "user";
    private static final String ID              = "id";
    private static final String EMAIL           = "email";
    private static final String USERNAME        = "username";
    private static final String PASSWORD        = "password";
    private static final String REMOVAL_DATE    = "removal_date";
    private static final String GROUP_FK        = "group_fk";

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        return instance;
    }

    // TESTME: #44

    /**
     * @param user Required values: email, username, password <br>possible nullable: removal date and group with id
     * @return
     */
    public int insert(User user) {
        String method = "insert " + TABLE;
        int rows = -1;
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + EMAIL + "," + USERNAME + "," + PASSWORD + "," + REMOVAL_DATE + "," + GROUP_FK + ")"
                                        + " VALUES( ?, ?, ?, ?, ?);"
                                        , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,   user.getEmail());
            stmt.setString(2,   user.getUsername());
            stmt.setString(3,   user.getPassword());
            stmt.setDate(4,     user.getRemovalDate());
            Group group = user.getGroup();
            if (group != null)
                stmt.setInt(5, group.getId());
             else
                stmt.setNull(5, Types.INTEGER);

            rows = stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next())
                user.setId(generatedKeys.getInt(1));

            if (rows > 0)
                users.add(user);

        } catch (SQLException e) {
            rows = switchSQLError(method, e);
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
                if (closeCon){
                    con.close();
                }
            } catch (SQLException e) {
                logSQLError("closure " + method, e);
            }
        }
        return rows;
    }

    // TESTME: #44
    /**
     * @param username
     * @return user from database or null if not found / an error occurred <br>ignores removed user
     */
    public User selectByUsername(String username) {
        String method = "selectByUsername " + TABLE;

        User user               = null;
        Connection con          = getConnection(method);
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("SELECT " + ID + "," + EMAIL + "," + PASSWORD + "," + REMOVAL_DATE + "," + GROUP_FK + " FROM " + TABLE
                    + " WHERE " + USERNAME + " = ?"
                    + " AND " + REMOVAL_DATE + " = NULL;");
            stmt.setString(1, username);
            result = stmt.executeQuery();
            if (result.next()) {
                for (User savedUser : users) {
                    if (username == savedUser.getUsername()) {
                        user = savedUser;
                        break;
                    }
                }
                if (user == null) {
                    user = new User();

                    int groupFK = result.getInt(GROUP_FK);
                    if(groupFK != 0) {
                        // testme: #44 does group callback?
                        GroupDAO dao = DAOFactory.getGroupDAO();
                        user.setGroup(dao.selectById(groupFK));
                    }
                    users.add(user);
                }
                user.setUsername(username);
                user.setId(result.getInt(ID));
                user.setEmail(result.getString(EMAIL));
                user.setPassword(result.getString(PASSWORD));
                user.setRemovalDate(result.getDate(REMOVAL_DATE));
            }
        } catch (SQLException e) {
            logSQLError(method, e);
            user = null;
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
                logSQLError("closure selectByUsername " + TABLE, e);
            }
        }
        return user;
    }

    private void selectByEmail(String email) {
        // TODO: someday implement method
        // at the moment no required feature!
    }

    // TESTME: #44
    /**
     * @param group needs to contain at least the id
     * @return list of users from database or null if not found / an error occurred <br>ignores removed user
     */
    public List<User> selectAllByGroupId(Group group) {
        String method = "selectAllByGroupId " + TABLE;
        List<User> userList     = new ArrayList();
        int groupFk             = group.getId();
        Connection con          = getConnection(method);
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("SELECT " + ID + "," + EMAIL + "," + PASSWORD + "," + REMOVAL_DATE + "," + USERNAME + " FROM " + TABLE
                    + " WHERE " + GROUP_FK + " = ?"
                    + " AND " + REMOVAL_DATE + " = NULL;");
            stmt.setInt(1, groupFk);
            result = stmt.executeQuery();
            while (result.next()) {
                int id = result.getInt(ID);
                User user = null;
                for (User savedUser : users) {
                    if (id == savedUser.getId()) {
                        user = savedUser;
                        break;
                    }
                }
                if (user == null) {
                    user = new User();
                    users.add(user);
                }
                user.setId(id);
                user.setEmail(result.getString(EMAIL));
                user.setUsername(result.getString(USERNAME));
                user.setPassword(result.getString(PASSWORD));
                user.setRemovalDate(result.getDate(REMOVAL_DATE));
                user.setGroup(group);

                userList.add(user);
            }

        } catch (SQLException e) {
            logSQLError(method, e);
            userList = null;
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
                logSQLError("closure "+ method, e);
            }
        }
        if (!userList.isEmpty()) {
            return userList;
        } else {
            return null;
        }
    }

    // TESTME: #44
    /**
     * update group_fk in user
     * @param user needs to contain at least the id and a group with an id
     * @return
     */
    public int updateGroup(User user) {
        String method = "updateGroup " + TABLE;
        Group group             = user.getGroup();
        int rows                = -1;
        Connection con          = getConnection(method);
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("UPDATE " + TABLE
                    + " SET " + GROUP_FK + " = ?"
                    + " WHERE " + ID + " = ?"
                    + " AND " + REMOVAL_DATE + " = NULL;");
            if(group != null){
                stmt.setInt(1,  group.getId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setInt(2,  user.getId());

            rows = stmt.executeUpdate();

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
                logSQLError("closure "+method, e);
            }
        }
        return rows;
    }

    // TESTME: #44
    /**
     * @param user needs to contain at least the id
     * @return
     */
    public int remove(User user) {
        String method = "remove " + TABLE;
        Date removalDate        = new Date(new java.util.Date().getTime());
        int rows                = -1;
        Connection con          = getConnection(method);
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("UPDATE " + TABLE
                                        + " SET " + REMOVAL_DATE + " = ?"
                                        + " WHERE " + ID + " = ?;");
            stmt.setDate(1, removalDate);
            stmt.setInt(2,  user.getId());

            rows = stmt.executeUpdate();
            if (rows > 0) {
                user.setRemovalDate(removalDate);
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

    private void reactivate() {
        // TODO: someday implement method
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}