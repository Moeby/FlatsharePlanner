package com.tbz.mntn.flattie.database.dao;

public class DAOFactory {
    static public UserDAO getUserDAO() {
        return UserDAO.getInstance();
    }

    static public CalendarItemDAO getCalendarItemDAO() {
        return CalendarItemDAO.getInstance();
    }

    static public EventCategoryDAO getEventCategoryDAO() {
        return EventCategoryDAO.getInstance();
    }

    static public GroupDAO getGroupDAO() {
        return GroupDAO.getInstance();
    }

    static public RepEventExceptionDAO getRepEventExeptionDAO() {
        return RepEventExceptionDAO.getInstance();
    }

    static public ShoppingItemDAO getShoppingItemDAO() {
        return ShoppingItemDAO.getInstance();
    }
}
