package com.tbz.mntn.flattie.database.dataclasses;

import android.support.annotation.Nullable;

import java.sql.Timestamp;

public class RepEventException {

  private int          id;
  private Timestamp    startDatetime;
  private Timestamp    endDatetime;
  private boolean      skipped;
  private CalendarItem calendarItem;

  /**
   * Empty constructor. Do not forget to add all required fields.
   */
  public RepEventException() {
  }

  /**
   * Constructor to create a complete RepEventException.
   * @param startDatetime beginning of the event (enter time 0:00 for a hole day)
   * @param endDatetime   end of the event (if it last until 24:00 simply enter null)
   * @param skipped       true if the event is skipped for this date
   * @param calendarItem  associated CalendarItem
   */
  public RepEventException(Timestamp startDatetime, @Nullable Timestamp endDatetime,
                           boolean skipped, @Nullable CalendarItem calendarItem) {
    this.startDatetime = startDatetime;
    this.endDatetime = endDatetime;
    this.skipped = skipped;
    this.calendarItem = calendarItem;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Timestamp getStartDatetime() {
    return startDatetime;
  }

  public void setStartDatetime(Timestamp startDatetime) {
    this.startDatetime = startDatetime;
  }

  public Timestamp getEndDatetime() {
    return endDatetime;
  }

  public void setEndDatetime(@Nullable Timestamp endDatetime) {
    this.endDatetime = endDatetime;
  }

  public boolean isSkipped() {
    return skipped;
  }

  public void setSkipped(boolean skipped) {
    this.skipped = skipped;
  }

  public CalendarItem getCalendarItem() {
    return calendarItem;
  }

  public void setCalendarItem(@Nullable CalendarItem calendarItem) {
    this.calendarItem = calendarItem;
  }
}
