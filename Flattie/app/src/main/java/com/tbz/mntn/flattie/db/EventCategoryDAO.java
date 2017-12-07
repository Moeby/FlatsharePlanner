package com.tbz.mntn.flattie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// TODO: #44 INSERT CONNECTION IN ALL METHODS
public class EventCategoryDAO {
    private static EventCategoryDAO instance = new EventCategoryDAO();
    private ArrayList<EventCategory> eventCategories = new ArrayList();

    // table constants
    private static final String TABLE   = "event_category";
    private static final String ID      = "id";
    private static final String NAME    = "name";

    private EventCategoryDAO(){}

    public static EventCategoryDAO getInstance() {
        return instance;
    }

    // TESTME: #44
    public EventCategory selectById(int id){
        EventCategory category  = null;
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("SELECT " + NAME + " FROM " + TABLE
                                            + " WHERE " + ID + " = ?;");
            stmt.setInt(1, id);
            result = stmt.executeQuery();
            if(result.next()){
                for(EventCategory eventCategory: eventCategories){
                    if(id == eventCategory.getId()){
                        category = eventCategory;
                        break;
                    }
                }
                if(category != null) {
                    category.setId(id);
                    category.setName(result.getString(NAME));
                    eventCategories.add(category);
                }
            }else{
                category = null;
            }
        }catch (SQLException e){
            // TODO: #44 implement errorhandling
        }finally {
            try  {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                // TODO: #44 implement errorhandling
                System.out.println("Statement or result close failed");
            }
        }
        return category;
    }

    // TESTME: #44
    public List<EventCategory> selectAll(){
        List<EventCategory> categories  = new ArrayList();
        Connection con          = null;
        PreparedStatement stmt  = null;
        ResultSet result        = null;
        try {
            stmt = con.prepareStatement("SELECT " + ID + ", " + NAME + " FROM " + TABLE + ";");
            result = stmt.executeQuery();
            while(result.next()){
                int id = result.getInt(ID);
                EventCategory category = null;
                for(EventCategory eventCategory: eventCategories){
                    if(id == eventCategory.getId()){
                        category = eventCategory;
                        break;
                    }
                }
                if(category != null){
                    category = new EventCategory();
                    category.setId(id);
                    category.setName(result.getString(NAME));
    
                    eventCategories.add(category);
                }
                categories.add(category);
            }
        }catch (SQLException e){
            // TODO: #44 implement errorhandling
        }finally {
            try  {
                // free resources
                if (result != null)
                    result.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                // TODO: #44 implement errorhandling
                System.out.println("Statement or result close failed");
            }
        }
        if(!categories.isEmpty()) {
            return categories;
        }else{
            return null;
        }
    }
}
