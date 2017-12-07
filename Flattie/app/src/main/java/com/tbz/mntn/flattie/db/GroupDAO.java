package com.tbz.mntn.flattie.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {
    private static GroupDAO instance = new GroupDAO();
    private static List<Group> groups = new Group();

    // table constants
    private static final String TABLE = "group";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String REMOVAL_DATE = "removal_date";

    private GroupDAO(){}

    public static GroupDAO getInstance() {
        return instance;
    }

    // TESTME: #44
    public int insert(Group group){
        // TODO: #44 get connection
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try{
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + NAME + "," + REMOVAL_DATE + ")"
                            + " VALUES( ?, ?);"
                    , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, group.getName());
            stmt.setString(2, group.getRemovalDate());

            rows = stmt.executeUpdate();
            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    group.setId(generatedKeys.getInt(1));
                }
            }

            if(rows > 0){
                groups.add(group);
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

    // TESTME: #44
    public Group selectById(int id){
        // TODO: #44 get connection
        Group group             = null;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("SELECT " + NAME + " FROM " + TABLE
                                            + " WHERE " + ID + " = ?;");
            stmt.setInt(1, id);
            result = stmt.executeQuery();
            if(result.next()){
                for(Group savedGroup: groups){
                    if(id == savedGroup.getId()){
                        group = savedGroup;
                        break;
                    }
                }
                if(group != null) {
                    group.setId(id);
                    group.setName(result.getString(NAME));
                    groups.add(group);
                }
            }else{
                group = null;
            }
        }catch (SQLException e){
            // TODO: #44 implement errorhandling
        }finally {
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
        return group;
    }

    // TESTME: #44
    public int remove(Group group){
        // TODO: #44 get connection
        Date removalDate        = new Date(new java.util.Date().getTime());
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try{
            stmt = con.prepareStatement("UPDATE " + TABLE +
                            " SET " + REMOVAL_DATE + " = ?;");
            stmt.setDate(1, removalDate);

            rows = stmt.executeUpdate();

            if(rows > 0){
                // TODO: #44 check if needed
                groups.remove(group);
                group.setRemovalDate(removalDate);
                groups.add(group);
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

    // TESTME: #44
    public int reactivate(Group group){
        // at the moment no required feature!
        // TODO: #44 get connection
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try{
            stmt = con.prepareStatement("UPDATE " + TABLE +
                    " SET " + REMOVAL_DATE + " = ?;");
            stmt.setDate(1, null);

            rows = stmt.executeUpdate();

            if(rows > 0){
                // TODO: #44 check if needed
                groups.remove(group);
                group.setRemovalDate(null);
                groups.add(group);
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
