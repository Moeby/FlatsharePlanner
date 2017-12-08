package db;

import java.sql.Date;

public class User {

	private int id;
	private String email;
	private String username;
	private String password;
	private Date removalDate;
	private Group group;
	
	public User(int id, String email, String username, String password, Date removalDate, Group group) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.removalDate = removalDate;
		this.group = group;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getRemovalDate() {
		return removalDate;
	}

	public void setRemovalDate(Date removalDate) {
		this.removalDate = removalDate;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}
