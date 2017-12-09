package com.tbz.mntn.flattie.db;

import android.support.annotation.Nullable;

import java.util.ArrayList;

public class EventCategory {
    private int id;
    private String name;
    private ArrayList<CalendarItem> calendarItems;

    public EventCategory() {
    }

    /**
     * @param name
     * @param calendarItems
     */
    public EventCategory(String name, @Nullable ArrayList<CalendarItem> calendarItems) {
        this.name           = name;
        this.calendarItems  = calendarItems;
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
