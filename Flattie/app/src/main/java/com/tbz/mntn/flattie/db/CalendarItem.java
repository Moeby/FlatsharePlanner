package com.tbz.mntn.flattie.db;

import java.sql.Timestamp;
import java.util.ArrayList;

public class CalendarItem {

	private int id;
	private String description;
	// TODO: 08.12.2017 for #45: change to Enum
	private String repeatable;
	// TODO: 08.12.2017 for #45: change to Date
    private Timestamp startDatetime;
	// TODO: 08.12.2017 for #45: change to Date
	private Timestamp endDatetime;
	private Group group;
	private EventCategory eventCategory;
	private User user;
	private ArrayList<RepEventException> repEventExceptions;
	
	public CalendarItem(int id, String description, String repeatable, Timestamp startDatetime, Timestamp endDatetime,
			Group group, EventCategory eventCategory, User user, ArrayList<RepEventException> repEventExceptions) {
		this.id = id;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRepeatable() {
		return repeatable;
	}

	public void setRepeatable(String repeatable) {
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

	public void setEndDatetime(Timestamp endDatetime) {
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

	public void setUser(User user) {
		this.user = user;
	}

	public ArrayList<RepEventException> getRepEventExceptions() {
		return repEventExceptions;
	}

	public void setRepEventExceptions(ArrayList<RepEventException> repEventExceptions) {
		this.repEventExceptions = repEventExceptions;
	}
	
}
