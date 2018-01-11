package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.dataclasses.Group;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GroupDaoTest extends Assert {
    private GroupDao dao;

    private Group group;
    private String groupName;
    private int newID = 1;

    private int result;

    @Before
    public void setUp() throws Exception {
        dao = DaoFactory.getGroupDao();

        groupName = "name";

        group = new Group();
        group.setName(groupName);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Ignore
    @Test
    public void insert() throws Exception {
        System.out.println("insert");
        group.setRemovalDate(null);

        result = dao.insert(group);

        assertEquals(1,result);
        assertEquals(newID,group.getId());
    }

    @Ignore
    @Test
    public void selectById() throws Exception {
        System.out.println("selectById");
        Group group = dao.selectById(1);

        assertEquals(newID,group.getId());
        assertEquals(1,dao.getGroups().size());
        assertEquals(1,group.getUsers().size());
        assertEquals(2,group.getCalendarItems().size());
        assertEquals(1,group.getShoppingItems().size());
        
        // double select
        dao.selectById(1);
        assertEquals(1,dao.getGroups().size());
        assertEquals(1,group.getUsers().size());
        assertEquals(2,group.getCalendarItems().size());
        assertEquals(1,group.getShoppingItems().size());
    }

    @Ignore
    @Test
    public void remove() throws Exception {
        System.out.println("remove");

        group.setId(1);
        result = dao.remove(group);

        assertEquals(1,result);
        assertEquals(0,dao.getGroups().size());
    }

    @Ignore
    @Test
    public void selectByIdAfterRemove() throws Exception {
        System.out.println("selectById - after remove");
        Group group = dao.selectById(1);

        assertEquals(null,group);
        assertEquals(0,dao.getGroups().size());
    }
}