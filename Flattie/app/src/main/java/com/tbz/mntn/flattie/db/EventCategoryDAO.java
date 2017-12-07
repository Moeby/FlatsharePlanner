package com.tbz.mntn.flattie.db;

/**
 * Created by Nadja on 06.12.2017.
 */

public class EventCategoryDAO {
    private static EventCategoryDAO instance = new EventCategoryDAO();

    // table constants
    private static final String TABLE = "event_category";
    private static final String ID = "id";
    private static final String NAME = "name";


    private EventCategoryDAO(){}

    public static EventCategoryDAO getInstance() {
        return instance;
    }

    public void selectById(){
        // TODO: #44 implement method
    }

    public void selectAll(){
        // TODO: #44 implement method
    }
}
