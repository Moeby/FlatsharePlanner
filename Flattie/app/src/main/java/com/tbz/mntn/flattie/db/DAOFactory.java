package com.tbz.mntn.flattie.db;

/**
 * Created by Nadja on 06.12.2017.
 */

public class DAOFactory {
    static public UserDAO getUserDAO(){
        return UserDAO.getInstance();
    }
    static public CalendarItemDAO getCalendarItemDAO(){
        return CalendarItemDAO.getInstance();
    }
    static public EventCategoryDAO getEventCategoryDAO(){
        return EventCategoryDAO.getInstance();
    }
    static public GroupDAO getGroupDAO(){
        return GroupDAO.getInstance();
    }
    static public RepEventExeptionDAO getRepEventExeptionDAO(){
        return RepEventExeptionDAO.getInstance();
    }
    static public ShoppingItemDAO getShoppingItemDAO(){
        return ShoppingItemDAO.getInstance();
    }
}
