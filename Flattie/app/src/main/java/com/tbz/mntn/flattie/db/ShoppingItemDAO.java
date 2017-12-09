package com.tbz.mntn.flattie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

// TODO: #44 INSERT CONNECTION IN ALL METHODS
public class ShoppingItemDAO {
    private static ShoppingItemDAO instance         = new ShoppingItemDAO();
    private ArrayList<ShoppingItem> shoppingItems   = new ArrayList();

    // table constants
    private static final String TABLE       = "shopping_item";
    private static final String ID          = "id";
    private static final String NAME        = "name";
    private static final String BOUGHT      = "bought";
    private static final String GROUP_FK    = "group_fk";

    private ShoppingItemDAO() {
    }

    public static ShoppingItemDAO getInstance() {
        return instance;
    }

    // TESTME: #44
    public int insert(ShoppingItem shoppingItem) {
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + NAME + "," + BOUGHT + "," + GROUP_FK + ")"
                                        + " VALUES( ?, ?, ?);"
                                        , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,   shoppingItem.getName());
            stmt.setBoolean(2,  shoppingItem.isBought());
            Group group = shoppingItem.getGroup();
            if (group != null) {
                stmt.setInt(3, group.getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            rows = stmt.executeUpdate();
            try {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next())
                    shoppingItem.setId(generatedKeys.getInt(1));
            } catch (SQLException e) {
                // TODO: #44 implement errorhandling
            }

            if (rows > 0)
                shoppingItems.add(shoppingItem);

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

    // TOASK: #44 add update

    public void selectAllByGroupId() {
        // TODO: #44 implement method
        // select by foreign key group_fk
    }

    // TESTME: #44
    public int delete(ShoppingItem shoppingItem) {
        int rows                = -1;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("DELETE FROM " + TABLE
                                        + " WHERE " + ID + " = ?;"
                                        , Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, shoppingItem.getId());

            rows = stmt.executeUpdate();
            if (rows > 0)
                shoppingItems.remove(shoppingItem);

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
}
