package com.tbz.mntn.flattie.db;

public class ShoppingItem {

	private int id;
	private String name;
	private boolean bought;
	private Group group;
	
	public ShoppingItem(int id, String name, boolean bought, Group group) {
		this.id = id;
		this.name = name;
		this.bought = bought;
		this.group = group;
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

	public boolean isBought() {
		return bought;
	}

	public void setBought(boolean bought) {
		this.bought = bought;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
}
