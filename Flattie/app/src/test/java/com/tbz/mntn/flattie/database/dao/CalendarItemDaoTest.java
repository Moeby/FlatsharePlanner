package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.databaseConnection.MysqlConnector;
import com.tbz.mntn.flattie.database.dataclasses.CalendarItem;
import com.tbz.mntn.flattie.database.dataclasses.EventCategory;
import com.tbz.mntn.flattie.database.dataclasses.Group;
import com.tbz.mntn.flattie.database.dataclasses.Repeatable;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class CalendarItemDaoTest extends Assert {
    private CalendarItemDao dao;

    private CalendarItem item;
    private String description;
    private Timestamp startDate;
    private Timestamp endDate;
    private Repeatable repeatable;
    private EventCategory category;
    private Group group;
    private int newID = 1;

    private int result;

    @Before
    public void setUp() throws Exception {
        dao = DaoFactory.getCalendarItemDao();

        description = "This is a description";
        startDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        endDate = new java.sql.Timestamp(startDate.getTime() + 50000000);
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

    @Ignore
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

        assertEquals(1, result);
        assertEquals(newID, item.getId());
    }

    @Ignore
    @Test
    public void insertNullableValues() throws Exception {
        System.out.println("insert - nullable");

        item.setDescription(description);
        item.setStartDatetime(startDate);
        item.setRepeatable(repeatable);
        item.setEventCategory(category);
        item.setGroup(group);

        result = dao.insert(item);

        assertEquals(1, result);
        assertEquals(2, item.getId());
    }

    @Ignore
    @Test
    public void selectByIdHasNoCategory() throws Exception {
        System.out.println("selectById - HasNoCategory");
        CalendarItem item = dao.selectById(newID);

        assertEquals(newID, item.getId());
        assertEquals(2, dao.getCalendarItems().size());
        assertEquals(1, DaoFactory.getGroupDao().getGroups().size());
        assertEquals(1, DaoFactory.getEventCategoryDao().getEventCategories().size());
    }

    @Ignore
    @Test
    public void selectById() throws Exception {
        System.out.println("selectById");
        CalendarItem item = dao.selectById(newID);

        assertEquals(newID, item.getId());
        assertEquals(2, dao.getCalendarItems().size());
        assertEquals(1, DaoFactory.getGroupDao().getGroups().size());
        assertEquals(1, DaoFactory.getEventCategoryDao().getEventCategories().size());
        Connection con = MysqlConnector.getConnection();
        assertTrue(con.isClosed());

        System.out.println("first run done");

        // double selects
        item = dao.selectById(newID);
        assertEquals(newID, item.getId());
        assertEquals(2, dao.getCalendarItems().size());
    }

    @Ignore
    @Test
    public void selectAllByGroupId() throws Exception {
        System.out.println("selectAllByGroupId");
        Group group = new Group();
        group.setId(1);
        List<CalendarItem> items = dao.selectAllByGroupId(group);

        assertEquals(newID, items.get(0).getId());
        assertEquals(2, dao.getCalendarItems().size());

        // double selects
        items = dao.selectAllByGroupId(group);
        assertEquals(newID, items.get(0).getId());
        assertEquals(2, dao.getCalendarItems().size());
    }

    @Ignore
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
        item.setEventCategory(DaoFactory.getEventCategoryDao().selectById(1));
        Group group = new Group();
        group.setId(1);
        item.setGroup(group);

        result = dao.update(item);

        assertEquals(1, result);
    }

    @Ignore
    @Test
    public void delete() throws Exception {
        System.out.println("delete");

        item.setId(1);

        result = dao.delete(item);

        assertEquals(1, result);
        assertEquals(0, dao.getCalendarItems().size());
    }

    @Ignore
    @Test
    public void deleteWithoutException() throws Exception {
        System.out.println("delete - no exceptions");

        item.setId(2);

        result = dao.delete(item);

        assertEquals(1, result);
        assertEquals(0, dao.getCalendarItems().size());
    }
}