package db;

import java.util.ArrayList;

public class EventCategory {

	private int id;
	private String name;
	private ArrayList<CalendarItem> calendarItems;
	
	public EventCategory(int id, String name, ArrayList<CalendarItem> calendarItems) {
		super();
		this.id = id;
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

	public void setCalendarItems(ArrayList<CalendarItem> calendarItems) {
		this.calendarItems = calendarItems;
	}
	
}
