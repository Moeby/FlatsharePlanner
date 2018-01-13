package com.tbz.mntn.flattie.database.dataclasses;

import android.support.annotation.Nullable;

public class ShoppingItem {

  private int     id;
  private String  name;
  private boolean bought;
  private Group   group;


  /**
   * Empty constructor. Do not forget to add all required fields.
   */
  public ShoppingItem() {
  }

  /**
   * Constructor to create a complete ShoppingItem.
   * @param name   title of the ShoppingItem
   * @param bought true if somebody planned to buy this item
   * @param group  associated group
   */
  public ShoppingItem(String name, boolean bought, @Nullable Group group) {
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
