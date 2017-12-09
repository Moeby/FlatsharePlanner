package com.tbz.mntn.flattie.db;


public enum Repeatable {
    NONE, DAILY, WEEKLY, MONTHLY, YEARLY;

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