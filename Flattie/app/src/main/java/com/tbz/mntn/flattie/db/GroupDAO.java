package com.tbz.mntn.flattie.db;

/**
 * Created by Nadja on 06.12.2017.
 */

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
        // FIXME: 07.12.2017 add to ticket #44
        // at the moment no required feature!
    }
}
