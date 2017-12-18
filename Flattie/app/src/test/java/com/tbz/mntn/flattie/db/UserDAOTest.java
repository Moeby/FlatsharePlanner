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
    private String email2;
    private String username;
    private String username2;
    private String password;
    private int newID = 1;

    private int result;

    private int duplicate = -200;
    private int notNull = -500;

    @Before
    public void setUp() throws Exception {
        dao = DAOFactory.getUserDAO();

        email = "mail";
        email2 = "mail2";
        username = "name";
        username2 = "name2";
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
    public void insertGroupNull() throws Exception {
        System.out.println("insert - group null");

        user.setRemovalDate(null);
        Group group = new Group();
        group.setId(1);
        user.setGroup(group);

        result = dao.insert(user);

        assertEquals(1,result);
        assertEquals(newID,user.getId());
    }

    @Test
    public void insert() throws Exception {
        System.out.println("insert");

        user.setUsername(username2);
        user.setEmail(email2);
        user.setRemovalDate(null);
        user.setGroup(null);

        result = dao.insert(user);

        assertEquals(1,result);
        assertEquals(2,user.getId());
    }

    @Test
    public void insertDuplicateUsername() throws Exception {
        System.out.println("insert duplicate - username");

        user.setEmail(email2);
        user.setRemovalDate(null);
        user.setGroup(null);

        result = dao.insert(user);

        assertEquals(duplicate,result);
    }

    @Test
    public void insertDuplicateEmail() throws Exception {
        System.out.println("insert duplicate - email");

        user.setUsername(username2);
        user.setRemovalDate(null);
        user.setGroup(null);

        result = dao.insert(user);

        assertEquals(duplicate,result);
    }

    @Test
    public void insertWrongValue() throws Exception {
        System.out.println("insert wrong value");

        user.setUsername(null);
        user.setRemovalDate(null);
        user.setGroup(null);

        result = dao.insert(user);

        assertEquals(notNull,result);
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
    public void selectByUsernameHasNoGroup() throws Exception {
        System.out.println("selectByUsername - HasNoGroup");
        User user = dao.selectByUsername(username2);

        assertEquals(2,user.getId());
        assertEquals(1,dao.getUsers().size());
        assertEquals(0,DAOFactory.getGroupDAO().getGroups().size());
    }

    @Test
    public void selectAllByGroupId() throws Exception {
        System.out.println("selectAllByGroupId");
        Group group = new Group();
        group.setId(1);
        List<User> users = dao.selectAllByGroupId(group);

        assertEquals(newID,users.get(0).getId());
        assertEquals(1,dao.getUsers().size());
    }

    @Test
    public void selectAllByGroupIdNotFound() throws Exception {
        System.out.println("selectAllByGroupId - not found");
        Group group = new Group();
        group.setId(0);
        List<User> users = dao.selectAllByGroupId(group);

        assertEquals(null,users);
        assertEquals(0,dao.getUsers().size());
    }

    @Test
    public void updateGroup() throws Exception {
        System.out.println("updateGroup");

        Group group = new Group();
        group.setId(1);
        user.setGroup(group);
        user.setId(2);
        result = dao.updateGroup(user);

        assertEquals(1,result);
    }

    @Test
    public void updateGroupToNullable() throws Exception {
        System.out.println("updateGroup - null value");

        user.setId(1);
        user.setGroup(null);

        result = dao.updateGroup(user);

        assertEquals(1,result);
    }

    @Test
    public void removeNoGroup() throws Exception {
        user.setId(1);
        System.out.println("remove - no group");

        result = dao.remove(user);

        assertEquals(1,result);
    }

    @Test
    public void removeWithGroup() throws Exception {
        System.out.println("remove - with Group");
        User user2 = new User();
        Group group = new Group();
        group.setId(1);
        user2.setGroup(group);
        user2.setEmail("test");
        user2.setUsername("test");
        user2.setPassword("test");
        dao.insert(user2);

        result = dao.remove(user2);

        assertEquals(1,result);
    }

    @Test
    public void removeLastOfGroup() throws Exception {
        System.out.println("remove - last of Group");

        Group group = new Group();
        group.setId(1);
        user.setGroup(group);
        user.setId(2);

        result = dao.remove(user);

        assertEquals(1,result);
        assertEquals(null, DAOFactory.getGroupDAO().selectById(1));
    }

    @Test
    public void selectByIdAfterRemove() throws Exception {
        System.out.println("selectById - after remove");
        User user = dao.selectByUsername(username);

        assertEquals(null,user);
        assertEquals(0,dao.getUsers().size());
    }
}