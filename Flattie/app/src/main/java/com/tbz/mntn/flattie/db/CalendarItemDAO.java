package com.tbz.mntn.flattie.db;

public class CalendarItemDAO {
    private static CalendarItemDAO instance = new CalendarItemDAO();

    // table constants
    private static final String TABLE = "calendar_item";
    private static final String ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String REPEATABLE = "repeatable";
    private static final String START = "start_datetime";
    private static final String END = "end_datetime";
    private static final String GROUP_FK = "group_fk";
    private static final String EVENT_CATEGORY_FK = "event_category_fk";

    private CalendarItemDAO(){}

    public static CalendarItemDAO getInstance() {
        return instance;
    }

    public void insert(){
        // TODO: #44 implement method
        // don't insert a group_fk first, maybe change attribute in DB to nullable
        // removal_date = null
    }

    public void selectAllByGroupId() {
        // TODO: #44 implement method
        // select by foreign key group_fk
    }

    public void update(){
        // TODO: #44 implement method
        // what do with exceptions?
    }

    public void delete(){
        // TODO: #44 implement method
        // delete exceptions as well --> if repeatable != none
    }
}
