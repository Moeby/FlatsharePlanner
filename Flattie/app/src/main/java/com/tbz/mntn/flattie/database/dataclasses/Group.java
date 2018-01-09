package com.tbz.mntn.flattie.database.dataclasses;

import android.support.annotation.Nullable;

import java.sql.Date;
import java.util.ArrayList;

public class Group {
    private int id;
    private String name;
    private Date removalDate;
    private ArrayList<ShoppingItem> shoppingItems;
    private ArrayList<User> users;
    private ArrayList<CalendarItem> calendarItems;

    public Group() {

    }

    /**
     * @param name
     * @param removalDate
     * @param shoppingItems
     * @param users
     * @param calendarItems
     */
    public Group(String name, @Nullable Date removalDate, @Nullable ArrayList<ShoppingItem> shoppingItems,
                 @Nullable ArrayList<User> users, @Nullable ArrayList<CalendarItem> calendarItems) {
        this.name           = name;
        this.removalDate    = removalDate;
        this.shoppingItems  = shoppingItems;
        this.users          = users;
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

    public Date getRemovalDate() {
        return removalDate;
    }

    public void setRemovalDate(@Nullable Date removalDate) {
        this.removalDate = removalDate;
    }

    public ArrayList<ShoppingItem> getShoppingItems() {
        return shoppingItems;
    }

    public void setShoppingItems(@Nullable ArrayList<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(@Nullable ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<CalendarItem> getCalendarItems() {
        return calendarItems;
    }

    public void setCalendarItems(@Nullable ArrayList<CalendarItem> calendarItems) {
        this.calendarItems = calendarItems;
    }

}
