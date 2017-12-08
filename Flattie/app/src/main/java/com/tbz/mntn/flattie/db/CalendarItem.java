package db;

import java.sql.Timestamp;
import java.util.ArrayList;

public class CalendarItem {

	private int id;
	private String description;
	private String repeatable;
	private Timestamp startDatetime;
	private Timestamp endDatetime;
	private Group group;
	private EventCategory eventCategory;
	private User user;
	private ArrayList<RepEventExeption> repEventExeptions;
	
	public CalendarItem(int id, String description, String repeatable, Timestamp startDatetime, Timestamp endDatetime,
			Group group, EventCategory eventCategory, User user, ArrayList<RepEventExeption> repEventExeptions) {
		this.id = id;
		this.description = description;
		this.repeatable = repeatable;
		this.startDatetime = startDatetime;
		this.endDatetime = endDatetime;
		this.group = group;
		this.eventCategory = eventCategory;
		this.user = user;
		this.repEventExeptions = repEventExeptions;
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

	public ArrayList<RepEventExeption> getRepEventExeptions() {
		return repEventExeptions;
	}

	public void setRepEventExeptions(ArrayList<RepEventExeption> repEventExeptions) {
		this.repEventExeptions = repEventExeptions;
	}
	
}
