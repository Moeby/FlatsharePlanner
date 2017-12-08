package db;

import java.sql.Timestamp;

public class RepEventExeption {

	private int id;
	private Timestamp startDatetime;
	private Timestamp endDatetime;
	private boolean skipped;
	private CalendarItem calendarItem;
	
	public RepEventExeption(int id, Timestamp startDatetime, Timestamp endDatetime, boolean skipped,
			CalendarItem calendarItem) {
		this.id = id;
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
	public void setEndDatetime(Timestamp endDatetime) {
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
	public void setCalendarItem(CalendarItem calendarItem) {
		this.calendarItem = calendarItem;
	}
	
}
