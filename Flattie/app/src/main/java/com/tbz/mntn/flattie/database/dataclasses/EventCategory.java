package com.tbz.mntn.flattie.database.dataclasses;

import android.support.annotation.Nullable;

import java.util.ArrayList;

public class EventCategory {
  private int                     id;
  private String                  name;
  private ArrayList<CalendarItem> calendarItems;

  /**
   * Empty constructor. Do not forget to add all required fields.
   */
  public EventCategory() {
  }

  /**
   * Constructor to create a complete EventCategory.
   * @param name          category title
   * @param calendarItems associated events
   */
  public EventCategory(String name, @Nullable ArrayList<CalendarItem> calendarItems) {
    this.name = name;
    this.calendarItems = calendarItems;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<CalendarItem> getCalendarItems() {
    return calendarItems;
  }

  public void setCalendarItems(@Nullable ArrayList<CalendarItem> calendarItems) {
    this.calendarItems = calendarItems;
  }
}
