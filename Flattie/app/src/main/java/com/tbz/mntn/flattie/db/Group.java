package db;

import java.sql.Date;

public class Group {

	private int id;
	private String name;
	private Date removalDate;
	
	public Group(int id, String name, Date removalDate) {
		this.id = id;
		this.name = name;
		this.removalDate = removalDate;
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
	
}
