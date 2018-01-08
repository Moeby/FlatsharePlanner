package com.tbz.mntn.flattie.database.dataclasses;

import android.support.annotation.Nullable;

/**
 * Class for ShoppingItems.
 */
public class CSSunShoppingItem {

    /**
     * an id.
     */
    private int id;

    /**
     * a name.
     */
    private String name;

    /**
     * a bought.
     */
    private boolean bought;

    /**
     * a group.
     */
    private Group group;

    /**
     * Empty constructor which creates an empty ShoppingItem.
     * - set required attributes with getter and setter methods
     */
    public CSSunShoppingItem() {
    }

    /**
     * @param name   of ShoppingItem
     * @param bought true: the item gets bought, false: item is in list
     * @param group  which contains this ShoppingItem
     */
    public CSSunShoppingItem(final String name, final boolean bought,
                             @Nullable final Group group) {
        this.name = name;
        this.bought = bought;
        this.group = group;
    }

    /**
     * @return id of ShoppingItem
     */
    public int getId() {
        return id;
    }

    /**
     * @param id of ShoppingItem
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * @return name of ShoppingItem
     */
    public String getName() {
        return name;
    }

    /**
     * @param name of ShoppingItem
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return if ShoppingItem is bought
     */
    public boolean isBought() {
        return bought;
    }

    /**
     * @param bought of ShoppingItem
     */
    public void setBought(final boolean bought) {
        this.bought = bought;
    }

    /**
     * @return group of ShoppingItem
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @param group of ShoppingItem
     */
    public void setGroup(final Group group) {
        this.group = group;
    }
}
