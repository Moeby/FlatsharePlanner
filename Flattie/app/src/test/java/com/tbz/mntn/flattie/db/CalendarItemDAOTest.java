package com.tbz.mntn.flattie.db;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.List;

public class CalendarItemDAOTest extends Assert {
    private CalendarItemDAO dao;

    private CalendarItem item;
    private String description;
    private Date startDate;
    private Date endDate;
    private Repeatable repeatable;
    private EventCategory category;
    private Group group;
    private int newID = 1;

    private int result;

    @Before
    public void setUp() throws Exception {
        dao = DAOFactory.getCalendarItemDAO();

        description = "This is a description";
        startDate = new Date(new java.util.Date().getTime());

        endDate = new Date(startDate.getTime()+5000000);
        repeatable = Repeatable.NONE;
        category = new EventCategory();
        category.setId(1);
        
        group = new Group();
        group.setId(1);

        item = new CalendarItem();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insert() throws Exception {
        System.out.println("insert");

        item.setDescription(description);
        item.setStartDatetime(startDate);
        item.setEndDatetime(endDate);
        item.setRepeatable(Repeatable.DAILY);
        item.setEventCategory(category);
        item.setGroup(group);

        result = dao.insert(item);

        assertEquals(1,result);
        assertEquals(newID,item.getId());
    }

    @Test
    public void insertNullableValues() throws Exception {
        System.out.println("insert - nullable");

        item.setDescription(description);
        item.setStartDatetime(startDate);
        item.setRepeatable(repeatable);
        item.setEventCategory(category);
        item.setGroup(group);

        result = dao.insert(item);

        assertEquals(1,result);
        assertEquals(2,item.getId());
    }

    @Test
    public void selectByIdHasNoCategory() throws Exception {
        System.out.println("selectById - HasNoCategory");
        CalendarItem item = dao.selectById(newID);

        assertEquals(newID,item.getId());
        assertEquals(1,dao.getCalendarItems().size());
        assertEquals(1,DAOFactory.getGroupDAO().getGroups().size());
        assertEquals(0,DAOFactory.getEventCategoryDAO().getEventCategories().size());
    }

    @Test
    public void selectById() throws Exception {
        System.out.println("selectById");
        CalendarItem item = dao.selectById(newID);
    
        assertEquals(newID,item.getId());
        assertEquals(2,dao.getCalendarItems().size());
        assertEquals(1,DAOFactory.getGroupDAO().getGroups().size());
        assertEquals(1,DAOFactory.getEventCategoryDAO().getEventCategories().size());
    
        // double selects
        item = dao.selectById(newID);
        assertEquals(newID,item.getId());
        assertEquals(2,dao.getCalendarItems().size());
        assertEquals(1,DAOFactory.getGroupDAO().getGroups().size());
        assertEquals(1,DAOFactory.getEventCategoryDAO().getEventCategories().size());
    }
    
    @Test
    public void selectAllByGroupId() throws Exception {
        System.out.println("selectAllByGroupId");
        Group group = new Group();
        group.setId(1);
        List<CalendarItem> items = dao.selectAllByGroupId(group);
    
        assertEquals(newID,items.get(0).getId());
        assertEquals(2,dao.getCalendarItems().size());
        assertEquals(1,DAOFactory.getGroupDAO().getGroups().size());
        
        // double selects
        items = dao.selectAllByGroupId(group);
        assertEquals(newID,items.get(0).getId());
        assertEquals(2,dao.getCalendarItems().size());
        assertEquals(1,DAOFactory.getGroupDAO().getGroups().size());
    }

    @Test
    public void update() throws Exception {
        System.out.println("update");

        description = "Updated description";
        repeatable = Repeatable.MONTHLY;

        item.setId(1);
        item.setDescription(description);
        item.setStartDatetime(startDate);
        item.setEndDatetime(endDate);
        item.setRepeatable(repeatable);
        item.setEventCategory(DAOFactory.getEventCategoryDAO().selectById(1));
        Group group = new Group();
        group.setId(1);
        item.setGroup(group);
        
        result = dao.update(item);

        assertEquals(1,result);
    }

    @Test
    public void delete() throws Exception {
        System.out.println("delete");
        
        item.setId(1);

        result = dao.delete(item);

        assertEquals(1,result);
        assertEquals(0,dao.getCalendarItems().size());
    }
    
    
    @Test
    public void deleteWithoutException() throws Exception {
        System.out.println("delete - no exceptions");
        
        item.setId(2);
        
        result = dao.delete(item);
        
        assertEquals(1,result);
        assertEquals(0,dao.getCalendarItems().size());
    }
}