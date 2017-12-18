package com.tbz.mntn.flattie.db;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GroupDAOTest extends Assert {
    private GroupDAO dao;

    private Group group;
    private String groupName;
    private int newID = 1;

    private int result;

    @Before
    public void setUp() throws Exception {
        dao = DAOFactory.getGroupDAO();

        groupName = "name";

        group = new Group();
        group.setName(groupName);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insert() throws Exception {
        System.out.println("insert");
        group.setRemovalDate(null);

        result = dao.insert(group);

        assertEquals(1,result);
        assertEquals(newID,group.getId());
    }

    @Test
    public void selectById() throws Exception {
        System.out.println("selectById");
        Group group = dao.selectById(1);

        assertEquals(newID,group.getId());
        assertEquals(1,dao.getGroups().size());

        // check double-select
        dao.selectById(1);
        assertEquals(1,dao.getGroups().size());
    }

    @Test
    public void remove() throws Exception {
        System.out.println("remove");

        group.setId(1);
        result = dao.remove(group);

        assertEquals(1,result);
        assertEquals(0,dao.getGroups().size());
    }

    @Test
    public void selectByIdAfterRemove() throws Exception {
        System.out.println("selectById - after remove");
        Group group = dao.selectById(1);

        assertEquals(null,group);
        assertEquals(0,dao.getGroups().size());}
}