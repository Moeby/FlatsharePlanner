package com.tbz.mntn.flattie.db;

import java.security.acl.Group;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// TODO: #44 INSERT CONNECTION IN ALL METHODS
public class UserDAO {
    private static UserDAO instance = new UserDAO();
    private ArrayList<User> users = new ArrayList();

    // table constants
    private static final String TABLE = "user";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String REMOVAL_DATE = "removal_date";
    private static final String GROUP_FK = "group_fk";

    private UserDAO(){}

    public static UserDAO getInstance() {
        return instance;
    }

    // TESTME: #44
    public int insert(User user){
        // don't insert a group_fk first, maybe change attribute in DB to nullable
        // removal_date = null

        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try{
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + EMAIL + "," + USERNAME + "," + PASSWORD + "," + REMOVAL_DATE + "," + GROUP_FK + ")"
                            + " VALUES( ?, ?, ?, ?, ?);"
                    , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setDate(4, user.getRemovalDate());
            Group userGroup = user.getGroup();
            if(userGroup != null){
                stmt.setInt(5, user.getGroup().getId());
            }else{
                // FIXME: #44 How to add nullable with prepared statements?
                stmt.setString(5, null);
            }

            rows = stmt.executeUpdate();
            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    user.setId(generatedKeys.getInt(1));
                }
            }

            if(rows > 0){
                users.add(user);
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

    public void selectByUsername(String username){
        // TODO: #44 implement method
    }

    public void selectByEmail(String email){
        // TODO: someday implement method
        // at the moment no required feature!
    }

    public void selectAllByGroupId(Group group){
        // TODO: #44 implement method
        // select by foreign key group_fk
    }

    public void updateGroup(Group group){
        // TODO: #44 implement method
        // update group_fk when user is removed from / added to its group
    }

    // TESTME: #44
    public int remove(User user){
        Date removalDate        = new Date(new java.util.Date().getTime());
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try{
            stmt = con.prepareStatement("UPDATE " + TABLE
                                        + " SET " + REMOVAL_DATE + " = ?"
                    +   " WHERE " + ID + " = ?;");
            stmt.setDate(1, removalDate);
            stmt.setInt(2, user.getId());

            rows = stmt.executeUpdate();
            if(rows > 0){
                // TODO: #44 check if needed
                users.remove(user);
                user.setRemovalDate(removalDate);
                users.add(user);
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

    public void reactivate(){
        // TODO: someday implement method
        // #44 at the moment no required feature!
    }
}