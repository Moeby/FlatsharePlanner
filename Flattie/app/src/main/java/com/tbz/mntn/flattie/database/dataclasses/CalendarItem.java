package com.tbz.mntn.flattie.database.dataclasses;

import android.support.annotation.Nullable;

import java.sql.Timestamp;
import java.util.ArrayList;

public class CalendarItem {
    private int id;
    private String description;
    private Repeatable repeatable;
    private Timestamp startDatetime;
    private Timestamp endDatetime;
    private Group group;
    private EventCategory eventCategory;
    private User user;
    private ArrayList<RepEventException> repEventExceptions;

    public CalendarItem() {
    }

    /**
     * @param description
     * @param repeatable
     * @param startDatetime
     * @param endDatetime
     * @param group
     * @param eventCategory
     * @param user
     */
    public CalendarItem(@Nullable String description, Repeatable repeatable, Timestamp startDatetime, @Nullable Timestamp endDatetime,
                        Group group, EventCategory eventCategory, @Nullable User user, @Nullable ArrayList<RepEventException> repEventExceptions) {
        this.description = description;
        this.repeatable = repeatable;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.group = group;
        this.eventCategory = eventCategory;
        this.user = user;
        this.repEventExceptions = repEventExceptions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public Repeatable getRepeatable() {
        return repeatable;
    }

    public void setRepeatable(Repeatable repeatable) {
        this.repeatable = repeatable;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
    }

    public ArrayList<RepEventException> getRepEventExceptions() {
        return repEventExceptions;
    }

    public void setRepEventExceptions(@Nullable ArrayList<RepEventException> repEventExceptions) {
        this.repEventExceptions = repEventExceptions;
    }
}
