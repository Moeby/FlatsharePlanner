package com.tbz.mntn.flattie.db;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ShoppingItemDAOTest extends Assert {
    private ShoppingItemDAO dao;

    private ShoppingItem item;
    private String name;
    private boolean bought;
    private Group group;
    private int newID = 1;

    private int result;

    @Before
    public void setUp() throws Exception {
        item = new ShoppingItem();

        name = "name";
        bought = false;
        group = new Group();
        group.setId(newID);

        item = new ShoppingItem();

        item.setName(name);
        item.setBought(bought);
        item.setGroup(group);

        dao = DAOFactory.getShoppingItemDAO();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insert() throws Exception {
        System.out.println("insert");

        result = dao.insert(item);

        assertEquals(1,result);
        assertEquals(newID,item.getId());
    }

    @Test
    public void selectAllByGroupId() throws Exception {
        System.out.println("selectAllByGroupId");
        Group group = new Group();
        group.setId(1);
        List<ShoppingItem> items = dao.selectAllByGroupId(group);

        assertEquals(newID,items.get(0).getId());
        assertEquals(1,dao.getShoppingItems().size());
        
        // double select
        items = dao.selectAllByGroupId(group);
        assertEquals(newID,items.get(0).getId());
        assertEquals(1,dao.getShoppingItems().size());
    }

    @Test
    public void updateBought() throws Exception {
        System.out.println("updateBought");

        item.setBought(true);
        item.setId(1);
        result = dao.updateBought(item);

        assertEquals(1,result);
    }

    @Test
    public void delete() throws Exception {
        System.out.println("delete");

        item.setId(1);
        result = dao.delete(item);

        assertEquals(1,result);
    }
}