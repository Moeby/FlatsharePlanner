package com.tbz.mntn.flattie.db;

import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ShoppingItemDAO {
    private static ShoppingItemDAO instance = new ShoppingItemDAO();
    private ArrayList<ShoppingItem> shoppingItems = new ArrayList();

    // table constants
    private static final String TABLE = "shopping_item";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String BOUGHT = "bought";
    private static final String GROUP_FK = "group_fk";

    private ShoppingItemDAO(){}

    public static ShoppingItemDAO getInstance() {
        return instance;
    }

    // TESTME: #44
    public int insert(ShoppingItem shoppingItem){
        // TODO: #44 get connection
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try{
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + NAME + "," + BOUGHT + "," + GROUP_FK + ")"
                            + " VALUES( ?, ?, ?, ?, ?);"
                    , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, shoppingItem.getName());
            stmt.setInt(2, shoppingItem.getBought());

            Group userGroup = shoppingItem.getGroup();
            if(userGroup != null){
                stmt.setInt(3, shoppingItem.getGroup().getId());
            }else{
                // FIXME: #44 How to add nullable with prepared statements?
                stmt.setString(3, null);
            }

            rows = stmt.executeUpdate();
            try(ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    shoppingItem.setId(generatedKeys.getInt(1));
                }
            }

            if(rows > 0){
                shoppingItems.add(shoppingItem);
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

    // TODO: #44 add update

    public void selectAllByGroupId(){
        // TODO: #44 implement method
        // select by foreign key group_fk
    }

    // TESTME: #44
    public int delete(ShoppingItem shoppingItem){
        // TODO: #44 get connection
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try{
            stmt = con.prepareStatement("DELETE FROM " + TABLE
                            + " WHERE " + ID + " = ?;"
                    , Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, shoppingItem.getId());

            rows = stmt.executeUpdate();

            if(rows > 0){
                shoppingItems.remove(shoppingItem);
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
