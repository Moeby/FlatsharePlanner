package com.tbz.mntn.flattie.db;

/**
 * Created by Nadja on 06.12.2017.
 */

public class RepEventExeptionDAO {
    private static RepEventExeptionDAO instance = new RepEventExeptionDAO();

    // table constants
    private static final String TABLE = "rep_event_exeption";
    private static final String ID = "id";
    private static final String START = "start_datetime";
    private static final String END = "end_datetime";
    private static final String SKIPPED = "skipped";
    private static final String CALENDAR_ITEM_FK = "calendar_item_fk";


    private RepEventExeptionDAO(){}

    public static RepEventExeptionDAO getInstance() {
        return instance;
    }

    public void insert(){
        // TODO: #44 implement method
    }

    public void selectByCalendarItem(){
        // TODO: #44 implement method
    }

    public void update(){
        // TODO: #44 implement method
    }

    public void delete(){
        // TODO: #44 implement method
    }
}
