package com.tbz.mntn.flattie.database.dao;

public class DaoFactory {
  public static UserDao getUserDao() {
    return UserDao.getInstance();
  }

  public static CalendarItemDao getCalendarItemDao() {
    return CalendarItemDao.getInstance();
  }

  public static EventCategoryDao getEventCategoryDao() {
    return EventCategoryDao.getInstance();
  }

  public static GroupDao getGroupDao() {
    return GroupDao.getInstance();
  }

  public static RepEventExceptionDao getRepEventExeptionDao() {
    return RepEventExceptionDao.getInstance();
  }

  public static ShoppingItemDao getShoppingItemDao() {
    return ShoppingItemDao.getInstance();
  }
}
