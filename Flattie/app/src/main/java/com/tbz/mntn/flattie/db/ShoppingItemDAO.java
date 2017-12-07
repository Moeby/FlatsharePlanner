package com.tbz.mntn.flattie.db;

public class ShoppingItemDAO {
    private static ShoppingItemDAO instance = new ShoppingItemDAO();

    // table constants
    private static final String TABLE = "shopping_item";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String BOUGHT = "bought";
    private static final String GROUP_FK = "group_fk";

    private ShoppingItemDAO(){}

    public static ShoppingItemDAO getInstance() {
        return instance;
    }

    public void insert(){
        // TODO: #44 implement method
        // don't insert a group_fk first, maybe change attribute in DB to nullable
        // removal_date = null
    }

    public void selectAllByGroupId(){
        // TODO: #44 implement method
        // select by foreign key group_fk
    }

    public void delete(){
        // TODO: #44 implement method
    }
}
