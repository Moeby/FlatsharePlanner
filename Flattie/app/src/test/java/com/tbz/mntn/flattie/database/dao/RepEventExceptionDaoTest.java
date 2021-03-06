package com.tbz.mntn.flattie.database.dao;

import com.tbz.mntn.flattie.database.dataclasses.CalendarItem;
import com.tbz.mntn.flattie.database.dataclasses.RepEventException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class RepEventExceptionDaoTest extends Assert {
    private RepEventExceptionDao dao;

    private RepEventException exception;
    private boolean skipped;
    private Timestamp startDate;
    private Timestamp endDate;
    private CalendarItem calendarItem;
    private int newID = 1;

    private int result;

    @Before
    public void setUp() throws Exception {
        dao = DaoFactory.getRepEventExeptionDao();

        skipped = true;
        startDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        endDate = new java.sql.Timestamp(startDate.getTime() + 500000);
        calendarItem = new CalendarItem();
        calendarItem.setId(newID);

        exception = new RepEventException();
        exception.setStartDatetime(startDate);
        exception.setEndDatetime(endDate);
        exception.setSkipped(skipped);
        exception.setCalendarItem(calendarItem);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Ignore
    @Test
    public void insert() throws Exception {
        System.out.println("insert");

        result = dao.insert(exception);

        assertEquals(1,result);
        assertEquals(newID,exception.getId());
    }

    @Ignore
    @Test
    public void selectAllByCalendarItem() throws Exception {
        System.out.println("selectAllByCalendarItem");
        CalendarItem item = new CalendarItem();
        item.setId(1);
        List<RepEventException> exceptions = dao.selectAllByCalendarItem(item);

        assertEquals(newID,exceptions.get(0).getId());
        assertEquals(1,dao.getRepEventExceptions().size());
    
        // double select
        exceptions = dao.selectAllByCalendarItem(item);
        assertEquals(newID,exceptions.get(0).getId());
        assertEquals(1,dao.getRepEventExceptions().size());
    }

    @Ignore
    @Test
    public void update() throws Exception {
        System.out.println("update");

        skipped = false;
        exception.setSkipped(skipped);
        exception.setId(1);

        result = dao.update(exception);

        assertEquals(1,result);
    }

    @Ignore
    @Test
    public void delete() throws Exception {
        System.out.println("delete");
        exception.setId(1);
    
        result = dao.delete(exception);

        assertEquals(1,result);
        assertEquals(0,dao.getRepEventExceptions().size());
    }

    @Ignore
    @Test
    public void deleteAllByCalendarItem() throws Exception {
        System.out.println("deleteAllByCalendarItem");

        result = dao.deleteAllByCalendarItem(exception.getCalendarItem());

        assertEquals(1,result);
        assertEquals(0,dao.getRepEventExceptions().size());

    }

}