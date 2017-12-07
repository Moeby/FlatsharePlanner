package com.tbz.mntn.flattie.db;

public class UserDAO {
    private static UserDAO instance = new UserDAO();

    // table constants
    private static final String TABLE = "user";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String REMOVAL_DATE = "removal_date";
    private static final String GROUP_FK = "group_fk";

    private UserDAO(){}

    public static UserDAO getInstance() {
        return instance;
    }

    public void insert(){
        // TODO: #44 implement method
        // don't insert a group_fk first, maybe change attribute in DB to nullable
        // removal_date = null
    }

    public void selectByUsername(){
        // TODO: #44 implement method
    }

    public void selectByEmail(){
        // TODO: #44 implement method
        // at the moment no required feature!
    }

    public void selectAllByGroupId(){
        // TODO: #44 implement method
        // select by foreign key group_fk
    }

    public void updateGroup(){
        // TODO: #44 implement method
        // FIXME: 07.12.2017 add to ticket #44
        // update group_fk when user is removed from / added to its group
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