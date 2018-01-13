package com.tbz.mntn.flattie.calendar;

import com.tbz.mntn.flattie.database.dao.CalendarItemDao;
import com.tbz.mntn.flattie.database.dao.DaoFactory;
import com.tbz.mntn.flattie.database.dao.EventCategoryDao;
import com.tbz.mntn.flattie.database.dao.UserDao;
import com.tbz.mntn.flattie.database.dataclasses.CalendarItem;
import com.tbz.mntn.flattie.database.dataclasses.EventCategory;
import com.tbz.mntn.flattie.database.dataclasses.Group;
import com.tbz.mntn.flattie.database.dataclasses.Repeatable;
import com.tbz.mntn.flattie.database.dataclasses.User;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Calendar entry handling.
 */
public class CalendarEntryController {

  /**
   * Get the data objects for the specified parameters and try to persist it.
   *
   * @param description of the event
   * @param repeatable (NONE, DAILY, WEEKLY, MONTHLY, YEARLY)
   * @param startDate of the event
   * @param endDate of the event
   * @param group the event belongs to
   * @param eventCategory (event, absence, duty)
   * @param assignedUser to the event, only one possible
   * @return true if persisting was successful, false otherwise
   */
  public boolean saveCalendarEntryToDatabase(String description,
                                             String repeatable,
                                             Date startDate,
                                             Date endDate,
                                             Group group,
                                             String eventCategory,
                                             String assignedUser) {
    {
      CalendarItemDao calendarItemDao = DaoFactory.getCalendarItemDao();
      Repeatable      repeat          = Repeatable.toRepeatable(repeatable);

      EventCategoryDao    eventCategoryDao = DaoFactory.getEventCategoryDao();
      List<EventCategory> categories       = eventCategoryDao.selectAll();
      EventCategory       category         = null;

      UserDao   userDao        = DaoFactory.getUserDao();
      User      assigned       = userDao.selectByUsername(assignedUser);
      Timestamp startTimestamp = convertUtilDateToSqlTimestamp(startDate);
      Timestamp endTimestamp   = convertUtilDateToSqlTimestamp(endDate);

      for (EventCategory cat : categories) {
        if (eventCategory.equals(cat.getName())) {
          category = cat;
          break;
        }
      }

      CalendarItem item = new CalendarItem(description,
                                           repeat,
                                           startTimestamp,
                                           endTimestamp,
                                           group,
                                           category,
                                           assigned,
                                           null);
      int result = calendarItemDao.insert(item);

      // insert successful == 1
      if (result == 1) {
        return true;
      }
      return false;
    }
  }

  private Timestamp convertUtilDateToSqlTimestamp(Date dateToConvert) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(dateToConvert);
    cal.set(Calendar.MILLISECOND, 0);
    return new java.sql.Timestamp(dateToConvert.getTime());
  }
}
