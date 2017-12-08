package com.tbz.mntn.flattie.db;

import java.sql.Date;
import java.util.ArrayList;

public class Group {

	private int id;
	private String name;
	private Date removalDate;
	private ArrayList<ShoppingItem> shoppingItems;
	private ArrayList<User> users;
	private ArrayList<CalendarItem> calendarItems;
	
	public Group(int id, String name, Date removalDate, ArrayList<ShoppingItem> shoppingItems, ArrayList<User> users,
			ArrayList<CalendarItem> calendarItems) {
		this.id = id;
		this.name = name;
		this.removalDate = removalDate;
		this.shoppingItems = shoppingItems;
		this.users = users;
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

	public Date getRemovalDate() {
		return removalDate;
	}

	public void setRemovalDate(Date removalDate) {
		this.removalDate = removalDate;
	}

	public ArrayList<ShoppingItem> getShoppingItems() {
		return shoppingItems;
	}

	public void setShoppingItems(ArrayList<ShoppingItem> shoppingItems) {
		this.shoppingItems = shoppingItems;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public ArrayList<CalendarItem> getCalendarItems() {
		return calendarItems;
	}

	public void setCalendarItems(ArrayList<CalendarItem> calendarItems) {
		this.calendarItems = calendarItems;
	}
	
}
