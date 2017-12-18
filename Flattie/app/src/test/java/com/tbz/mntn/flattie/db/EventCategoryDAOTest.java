package com.tbz.mntn.flattie.db;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class EventCategoryDAOTest extends Assert {
    private EventCategoryDAO dao;

    private int newID = 1;

    @Before
    public void setUp() throws Exception {
        dao = DAOFactory.getEventCategoryDAO();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void selectById() throws Exception {
        System.out.println("selectById");

        EventCategory category = dao.selectById(newID);

        assertEquals(newID,category.getId());
        assertEquals(1,dao.getEventCategories().size());
    }

    @Test
    public void selectAll() throws Exception {
        System.out.println("selectById");

        List<EventCategory> categories = dao.selectAll();

        assertEquals(1,categories);
        assertEquals(1,dao.getEventCategories().size());
    }
}