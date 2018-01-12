package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.databaseConnection.MysqlConnector;
import com.tbz.mntn.flattie.database.dataclasses.Group;
import com.tbz.mntn.flattie.database.dataclasses.ShoppingItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ShoppingItemDao extends Dao {
  private static ShoppingItemDao         instance      = new ShoppingItemDao();
  private        ArrayList<ShoppingItem> shoppingItems = new ArrayList();
  private List<ShoppingItem> itemList;
  private int rows;

  // table constants
  private static final String TABLE = "shopping_item";
  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String BOUGHT = "bought";
  private static final String GROUP_FK = "group_fk";

  private ShoppingItemDao() {
  }

  public static ShoppingItemDao getInstance() {
    return instance;
  }

  /**
   * @param shoppingItem required values: name, group with id <br> default: bought = false, can also be set to true
   * @return
   */
  public int insert(final ShoppingItem shoppingItem) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "insert " + TABLE;
            rows = -1;
            Connection con = getConnection(method);
            PreparedStatement stmt = null;
            ResultSet result = null;
            try {
              stmt = con.prepareStatement("INSERT INTO " + TABLE + " (" + NAME + "," + BOUGHT + "," + GROUP_FK + ")"
                      + " VALUES( ?, ?, ?);"
                  , Statement.RETURN_GENERATED_KEYS);
              stmt.setString(1, shoppingItem.getName());
              stmt.setBoolean(2, shoppingItem.isBought());
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
              rows = switchSqlError(method, e);
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
                logSqlError("closure " + method, e);
              }
            }
          }
        });

    try {
      thread.start();
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return rows;
  }

  /**
   * @param group required values: id
   * @return list of shopping items from database or null if not found / an error occurred
   */
  public List<ShoppingItem> selectAllByGroupId(final Group group) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "selectAllByGroupId " + TABLE;
            itemList = new ArrayList();
            int groupFk = group.getId();
            Connection con = getConnection(method);
            PreparedStatement stmt = null;
            ResultSet result = null;
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
              logSqlError(method, e);
              MysqlConnector.close();
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
                logSqlError("closure " + method, e);
              }
            }
          }
        });

    try {
      thread.start();
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (!itemList.isEmpty()) {
      return itemList;
    } else {
      return null;
    }
  }

  /**
   * @param item required values: id, bought <br> ignored values: everything else
   * @return
   */
  public int updateBought(final ShoppingItem item) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "updateBought " + TABLE;
            rows = -1;
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
              rows = switchSqlError(method, e);
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
                logSqlError("closure " + method, e);
              }
            }
          }
        });

    try {
      thread.start();
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return rows;
  }

  /**
   * @param shoppingItem required values: id
   * @return
   */
  public int delete(final ShoppingItem shoppingItem) {
    Thread thread = new Thread(
        new Runnable() {
          public void run() {
            String method = "delete " + TABLE;
            rows = -1;
            Connection con = getConnection(method);
            PreparedStatement stmt = null;
            ResultSet result = null;
            try {
              stmt = con.prepareStatement("DELETE FROM " + TABLE
                      + " WHERE " + ID + " = ?;"
                  , Statement.RETURN_GENERATED_KEYS);
              stmt.setInt(1, shoppingItem.getId());

              rows = stmt.executeUpdate();
              if (rows > 0)
                shoppingItems.remove(shoppingItem);

            } catch (SQLException e) {
              rows = switchSqlError(method, e);
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
                logSqlError("closure " + method, e);
              }
            }
          }
        });

    try {
      thread.start();
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
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
