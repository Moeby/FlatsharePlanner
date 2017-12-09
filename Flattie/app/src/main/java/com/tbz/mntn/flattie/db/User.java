package com.tbz.mntn.flattie.db;

import android.support.annotation.Nullable;

import java.sql.Date;

public class User {

    private int id;
    private String email;
    private String username;
    private String password;
    private Date removalDate;
    private Group group;

    public User() {
    }

    /**
     * @param email
     * @param username
     * @param password
     * @param removalDate
     * @param group
     */
    public User(String email, String username, String password, @Nullable Date removalDate, @Nullable Group group) {
        this.email          = email;
        this.username       = username;
        this.password       = password;
        this.removalDate    = removalDate;
        this.group          = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRemovalDate() {
        return removalDate;
    }

    /**
     * @param removalDate
     */
    public void setRemovalDate(@Nullable Date removalDate) {
        this.removalDate = removalDate;
    }

    public Group getGroup() {
        return group;
    }

    /**
     * @param group
     */
    public void setGroup(@Nullable Group group) {
        this.group = group;
    }

}
