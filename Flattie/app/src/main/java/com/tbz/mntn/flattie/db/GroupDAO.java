package com.tbz.mntn.flattie.db;

public class GroupDAO {
    private static GroupDAO instance = new GroupDAO();

    // table constants
    private static final String TABLE = "group";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String REMOVAL_DATE = "removal_date";

    private GroupDAO(){}

    public static GroupDAO getInstance() {
        return instance;
    }

    public void insert(){
        // TODO: #44 implement method
        // removal_date = null
    }


    public void selectById(){
        // TODO: #44 implement method
    }

    public void remove(){
        // TODO: #44 implement method
        // update removal date --> insert current date
    }

    public void reactivate(){
        // TODO: #44 implement method
        // at the moment no required feature!
    }
}
