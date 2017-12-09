package com.tbz.mntn.flattie.db;

import android.support.annotation.Nullable;

import java.sql.Date;

public class RepEventException {

    private int id;
    private Date startDatetime;
    private Date endDatetime;
    private boolean skipped;
    private CalendarItem calendarItem;

    public RepEventException() {
    }

    /**
     * @param startDatetime
     * @param endDatetime
     * @param skipped
     * @param calendarItem
     */
    public RepEventException(Date startDatetime, @Nullable Date endDatetime, boolean skipped,
                             @Nullable CalendarItem calendarItem) {
        this.startDatetime  = startDatetime;
        this.endDatetime    = endDatetime;
        this.skipped        = skipped;
        this.calendarItem   = calendarItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(@Nullable Date endDatetime) {
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
