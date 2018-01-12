package com.tbz.mntn.flattie.database.dao;

public class DaoFactory {
    static public UserDao getUserDao() {
        return UserDao.getInstance();
    }

    static public CalendarItemDao getCalendarItemDao() {
        return CalendarItemDao.getInstance();
    }

    static public EventCategoryDao getEventCategoryDao() {
        return EventCategoryDao.getInstance();
    }

    static public GroupDao getGroupDao() {
        return GroupDao.getInstance();
    }

    static public RepEventExceptionDao getRepEventExeptionDao() {
        return RepEventExceptionDao.getInstance();
    }

    static public ShoppingItemDao getShoppingItemDao() {
        return ShoppingItemDao.getInstance();
    }
}
