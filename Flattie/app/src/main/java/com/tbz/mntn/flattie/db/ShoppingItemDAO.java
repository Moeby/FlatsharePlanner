package com.tbz.mntn.flattie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

// TODO: #44 INSERT CONNECTION IN ALL METHODS
public class ShoppingItemDAO extends DAO {
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
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next())
                shoppingItem.setId(generatedKeys.getInt(1));

            if (rows > 0)
                shoppingItems.add(shoppingItem);

        } catch (SQLException e) {
            rows = switchSQLError("insert ShoppingItem", e);
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                logSQLError("closure insert ShoppingItem", e);
            }
        }
        return rows;
    }

    // TESTME: #44
    // return null if not found
    public List<ShoppingItem> selectAllByGroupId(Group group) {
        List<ShoppingItem> itemList = new ArrayList();
        int groupFk                 = group.getId();
        Connection con              = null;
        PreparedStatement stmt      = null;
        ResultSet result            = null;
        try {
            stmt = con.prepareStatement("SELECT " + ID + "," + NAME + "," + BOUGHT + " FROM " + TABLE
                    + " WHERE " + GROUP_FK + " = ?;");
            stmt.setInt(1, groupFk);
            result = stmt.executeQuery();
            while (result.next()) {
                int id = result.getInt(ID);
                ShoppingItem item = null;
                for (ShoppingItem savedItem : shoppingItems) {
                    if (id == savedItem.getId()) {
                        item = savedItem;
                        break;
                    }
                }
                if (item == null) {
                    item = new ShoppingItem();
                    shoppingItems.add(item);
                }
                item.setId(id);
                item.setName(result.getString(NAME));
                item.setBought(result.getBoolean(BOUGHT));
                item.setGroup(group);

                itemList.add(item);
            }

        } catch (SQLException e) {
            logSQLError("selectAllByGroupId", e);
            itemList = null;
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                logSQLError("closure selectAllByGroupId ShoppingItem", e);
            }
        }
        if (!itemList.isEmpty()) {
            return itemList;
        } else {
            return null;
        }
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
            rows = switchSQLError("delete ShoppingItem", e);
        } finally {
            try {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                logSQLError("closure delete ShoppingItem", e);
            }
        }
        return rows;
    }

    public ArrayList<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(ArrayList<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }
}
