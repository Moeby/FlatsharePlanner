package com.tbz.mntn.flattie.db;

import com.tbz.mntn.flattie.databaseConnection.MysqlConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * @param shoppingItem required values: name, group with id <br> default: bought = false, can also be set to true
     * @return
     */
    public int insert(ShoppingItem shoppingItem) {
        String method = "insert " + TABLE;
        int rows                = -1;
        Connection con          = getConnection(method);
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + NAME + "," + BOUGHT + "," + GROUP_FK + ")"
                                        + " VALUES( ?, ?, ?);"
                                        , Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,   shoppingItem.getName());
            stmt.setBoolean(2,  shoppingItem.isBought());
            Group group = shoppingItem.getGroup();
            if (group != null && group.getId() != 0)
                stmt.setInt(3, group.getId());

            rows = stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next())
                shoppingItem.setId(generatedKeys.getInt(1));

            if (rows > 0)
                shoppingItems.add(shoppingItem);

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
    // return null if not found
    public List<ShoppingItem> selectAllByGroupId(Group group) {
        String method = "selectAllByGroupId " + TABLE;
        List<ShoppingItem> itemList = new ArrayList();
        int groupFk                 = group.getId();
        Connection con              = getConnection(method);
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
            logSQLError(method, e);
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

    //TESTME: #44
    /**
     * @param item required values: id, bought <br> ignored values: everything else
     * @return
     */
    public int updateBought(ShoppingItem item){
        String method = "updateBought " + TABLE;
        int rows                = -1;
        Connection con          = getConnection(method);
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("UPDATE " + TABLE
                    + " SET " + BOUGHT + " = ?"
                    + " WHERE " + ID + " = ?;");
            stmt.setBoolean(1,  item.isBought());
            stmt.setInt(2,      item.getId());

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
    public int delete(ShoppingItem shoppingItem) {
        String method = "delete " + TABLE;
        int rows                = -1;
        Connection con          = getConnection(method);
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

    public ArrayList<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(ArrayList<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }
}
