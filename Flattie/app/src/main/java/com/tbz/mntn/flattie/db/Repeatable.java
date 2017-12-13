package com.tbz.mntn.flattie.db;


public enum Repeatable {
    NONE, DAILY, WEEKLY, MONTHLY, YEARLY;

    public static Repeatable toRepeatable(String string){
        switch (string){
            case "daily":   return DAILY;
            case "weekly":  return WEEKLY;
            case "monthly": return MONTHLY;
            case "yearly":  return YEARLY;
        }
        return NONE;
    }

    @Override
    public String toString() {
        switch (this){
            case DAILY:     return "daily";
            case WEEKLY:    return "weekly";
            case MONTHLY:   return "monthly";
            case YEARLY:    return "yearly";
        }
        return "none";
    }
}