package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.dataclasses.EventCategory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class EventCategoryDAOTest extends Assert {
    private EventCategoryDAO dao;

    private int newID = 1;
    private String name = "event";

    @Before
    public void setUp() throws Exception {
        dao = DAOFactory.getEventCategoryDAO();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Ignore
    @Test
    public void selectById() throws Exception {
        System.out.println("selectById");

        EventCategory category = dao.selectById(newID);

        assertEquals(newID,category.getId());
        assertEquals(name,category.getName());
        assertEquals(1,dao.getEventCategories().size());
        
        // double selects
        category = dao.selectById(newID);
        assertEquals(newID,category.getId());
        assertEquals(name,category.getName());
        assertEquals(1,dao.getEventCategories().size());
    }

    @Ignore
    @Test
    public void selectAll() throws Exception {
        System.out.println("selectById");

        List<EventCategory> categories = dao.selectAll();

        assertEquals(newID,categories.get(0).getId());
        assertEquals(name,categories.get(0).getName());
        assertEquals(3,dao.getEventCategories().size());
    
        // double selects
        categories = dao.selectAll();
        assertEquals(newID,categories.get(0).getId());
        assertEquals(name,categories.get(0).getName());
        assertEquals(3,dao.getEventCategories().size());
    }
}