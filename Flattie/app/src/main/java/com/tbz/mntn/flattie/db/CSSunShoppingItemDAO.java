package com.tbz.mntn.flattie.db;

import com.tbz.mntn.flattie.databaseConnection.MysqlConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all DB access methods for a ShoppingItem.
 */
public final class CSSunShoppingItemDAO extends DAO {

    /**
     * an instance of this class.
     */
    private static CSSunShoppingItemDAO instance = new CSSunShoppingItemDAO();

    /**
     * a list of all shoppingitems which are loaded into the file.
     */
    private ArrayList<ShoppingItem> shoppingItems = new ArrayList();

    // table constants
    /**
     * Constant which contains the database table name.
     */
    private static final String TABLE = "shopping_item";

    /**
     * Constant for database attribute id.
     */
    private static final String ID = "id";

    /**
     * Constant for database attribute name.
     */
    private static final String NAME = "name";

    /**
     * Constant for database attribute bought.
     */
    private static final String BOUGHT = "bought";

    /**
     * Constant for database attribute group_fk.
     */
    private static final String GROUP_FK = "group_fk";

    /**
     * Empty Constructor.
     */
    private CSSunShoppingItemDAO() {
    }

    /**
     * Gets static instance.
     *
     * @return instance of CSSunShoppingItemDAO
     */
    public static CSSunShoppingItemDAO getInstance() {
        return instance;
    }

    /**
     * @param shoppingItem required values: name, group with id
     *                     <br> default: bought = false, can also be set to true
     * @return a number
     */
    public int insert(final ShoppingItem shoppingItem) {
        String method = "insert " + TABLE;
        int rows = -1;
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("INSERT INTO " + TABLE + " ("
                            + NAME + ","
                            + BOUGHT + ","
                            + GROUP_FK + ")"
                            + " VALUES( ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, shoppingItem.getName());
            stmt.setBoolean(2, shoppingItem.isBought());
            Group group = shoppingItem.getGroup();
            if (group != null && group.getId() != 0) {
                stmt.setInt(3, group.getId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            rows = stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                shoppingItem.setId(generatedKeys.getInt(1));
            }

            if (rows > 0) {
                shoppingItems.add(shoppingItem);
            }

        } catch (SQLException e) {
            rows = switchSQLError(method, e);
        } finally {
            try {
                // free resources
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (closeCon) {
                    MysqlConnector.close();
                }
            } catch (SQLException e) {
                logSQLError("closure " + method, e);
            }
        }
        return rows;
    }

    /**
     * @param group required values: id
     * @return list of shopping items from database
     * <br> null if not found / an error occurred
     */
    public List<ShoppingItem> selectAllByGroupId(final Group group) {
        String method = "selectAllByGroupId " + TABLE;
        List<ShoppingItem> itemList = new ArrayList();
        int groupFk = group.getId();
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("SELECT "
                    + ID + ","
                    + NAME + ","
                    + BOUGHT
                    + " FROM " + TABLE
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
            logSQLError(method, e);            MysqlConnector.close();
            itemList = null;
        } finally {
            try {
                // free resources
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (closeCon) {
                    MysqlConnector.close();
                }
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

    /**
     * @param item required values: id, bought
     *             <br> ignored values: everything else
     * @return a number
     */
    public int updateBought(final ShoppingItem item) {
        String method = "updateBought " + TABLE;
        int rows = -1;
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("UPDATE " + TABLE
                    + " SET " + BOUGHT + " = ?"
                    + " WHERE " + ID + " = ?;");
            stmt.setBoolean(1, item.isBought());
            stmt.setInt(2, item.getId());

            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            rows = switchSQLError(method, e);
        } finally {
            try {
                // free resources
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (closeCon) {
                    MysqlConnector.close();
                }
            } catch (SQLException e) {
                logSQLError("closure " + method, e);
            }
        }
        return rows;
    }

    /**
     * @param shoppingItem required values: id
     * @return a number
     */
    public int delete(final ShoppingItem shoppingItem) {
        String method = "delete " + TABLE;
        int rows = -1;
        Connection con = getConnection(method);
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = con.prepareStatement("DELETE FROM " + TABLE
                            + " WHERE " + ID + " = ?;",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, shoppingItem.getId());

            rows = stmt.executeUpdate();
            if (rows > 0) {
                shoppingItems.remove(shoppingItem);
            }

        } catch (SQLException e) {
            rows = switchSQLError(method, e);
        } finally {
            try {
                // free resources
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (closeCon) {
                    MysqlConnector.close();
                }
            } catch (SQLException e) {
                logSQLError("closure " + method, e);
            }
        }
        return rows;
    }

    /**
     * @return list of current ShoppingItems
     */
    public ArrayList<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }
}
