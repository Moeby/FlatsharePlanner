package com.tbz.mntn.flattie.db;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UserDAOTest extends Assert{
    private UserDAO dao;

    private User user;
    private String email;
    private String username;
    private String password;
    private int newID = 1;

    private int result;

    @Before
    public void setUp() throws Exception {
        dao = DAOFactory.getUserDAO();

        email = "mail";
        username = "name";
        password = "pw";

        user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insert() throws Exception {
        System.out.println("insert");

        user.setRemovalDate(null);
        user.setGroup(null);

        result = dao.insert(user);

        assertEquals(1,result);
        assertEquals(newID,user.getId());
    }

    @Test
    public void insertDuplicateUsername() throws Exception {
        System.out.println("insert duplicate - username");

        user.setEmail("another mail");
        user.setRemovalDate(null);
        user.setGroup(null);

        result = dao.insert(user);

        assertEquals(0,result);
        assertEquals(newID,user.getId());
    }

    @Test
    public void insertDuplicateEmail() throws Exception {
        System.out.println("insert duplicate - email");

        user.setUsername("another username");
        user.setRemovalDate(null);
        user.setGroup(null);

        result = dao.insert(user);

        assertEquals(0,result);
        assertEquals(newID,user.getId());
    }

    @Test
    public void insertWrongValue() throws Exception {
        System.out.println("insert wrong value");

        user.setUsername(null);
        user.setRemovalDate(null);
        user.setGroup(null);

        result = dao.insert(user);

        assertEquals(0,result);
        assertEquals(newID,user.getId());
    }

    @Test
    public void selectByUsernameHasNoGroup() throws Exception {
        System.out.println("selectByUsername - HasNoGroup");
        User user = dao.selectByUsername(username);

        assertEquals(newID,user.getId());
        assertEquals(1,dao.getUsers().size());
        assertEquals(0,DAOFactory.getGroupDAO().getGroups().size());
    }

    @Test
    public void selectByUsername() throws Exception {
        System.out.println("selectByUsername");
        User user = dao.selectByUsername(username);

        assertEquals(newID,user.getId());
        assertEquals(1,dao.getUsers().size());
        assertEquals(1,DAOFactory.getGroupDAO().getGroups().size());
    }

    @Test
    public void selectByEmail() throws Exception {
        // TODO: implement later
    }

    @Test
    public void selectAllByGroupId() throws Exception {
        System.out.println("selectAllByGroupId");
        Group group = new Group();
        group.setId(1);
        List<User> users = dao.selectAllByGroupId(group);

        assertEquals(newID,users.get(0).getId());
        assertEquals(1,dao.getUsers().size());
        assertEquals(1,DAOFactory.getGroupDAO().getGroups().size());
    }

    @Test
    public void updateGroup() throws Exception {
        System.out.println("updateGroup");

        Group group = new Group();
        group.setId(1);
        user.setGroup(group);

        result = dao.updateGroup(user);

        assertEquals(1,result);
        assertEquals(1,dao.getUsers().size());
        assertEquals(1,DAOFactory.getGroupDAO().getGroups().size());
    }

    @Test
    public void updateGroupToNullable() throws Exception {
        System.out.println("updateGroup - null value");

        Group group = null;
        user.setGroup(group);

        result = dao.updateGroup(user);

        assertEquals(1,result);
        assertEquals(1,dao.getUsers().size());
        assertEquals(1,DAOFactory.getGroupDAO().getGroups().size());
    }

    @Test
    public void remove() throws Exception {
        System.out.println("remove");

        result = dao.remove(user);

        assertEquals(1,result);
        assertEquals(0,dao.getUsers().size());
        assertEquals(1,DAOFactory.getGroupDAO().getGroups().size());
    }
}