package org.verzilin.servlet_api.dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.verzilin.servlet_api.config.JdbcConnectionProvider;
import org.verzilin.servlet_api.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDaoImplTest {
    UserDaoImpl userDao = new UserDaoImpl();

    @Container
    private PostgreSQLContainer psql = new PostgreSQLContainer("postgres:14.1-alpine")
            .withDatabaseName("foo")
            .withUsername("foo")
            .withPassword("secret");

    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUp() throws SQLException {
        // Clear table USERS
        Connection connection = JdbcConnectionProvider.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM users;TRUNCATE TABLE users RESTART IDENTITY CASCADE;");
        stmt.executeUpdate();
        connection.close();

        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("password1");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPassword("password2");

        user3 = new User();
        user3.setId(3L);
        user3.setUsername("user3");
        user3.setPassword("password3");
    }

    @AfterEach
    void clear() throws SQLException {
        String sqlRequest = "DELETE FROM users;TRUNCATE TABLE users RESTART IDENTITY CASCADE;";
        executeUpdateDB(sqlRequest);
    }

    @Test
    void testSaveUser() throws SQLException{

        userDao.saveUser(user1);

        String selectByUsername = "SELECT * FROM users WHERE username ='user1'";
        ResultSet rs = executeQueryDB(selectByUsername);
        if (rs.next()) {
            assertEquals("user1", rs.getString("username"));
            assertEquals("password1", rs.getString("password"));
        }
    }

    @Test
    void testUpdateUser() {
        userDao.saveUser(user1);//save new user


        user1.setPassword("newPassword1");
        user1.setUsername("newUsername1");
        userDao.updateUser(user1);//update new user

        User userFromDb = userDao.getById(1L);
        assertEquals("newUsername1", userFromDb.getUsername());
        assertEquals("newPassword1", userFromDb.getPassword());
    }

    @Test
    void testGetAllUsers() {
        userDao.saveUser(user1);
        userDao.saveUser(user2);
        userDao.saveUser(user3);

        List<User> users = userDao.getAllUsers();
        assertTrue(users.size() == 3);
    }

    @Test
    void testGetById(){
        userDao.saveUser(user1);

        User user = userDao.getById(1L);

        assertTrue(Objects.nonNull(user));
        assertTrue(user.getId() == 1);
        assertTrue(user.equals(user1));
    }

    @Test
    void testGetByIdWherePostNotFound(){
        User user = userDao.getById(15L);
        assertTrue(Objects.isNull(user));
    }

    @Test
    void testRemove() {
        userDao.remove(1l);
        User user = userDao.getById(1L);
        assertEquals(null, user);
    }

    private ResultSet executeQueryDB(String sqlRequest) throws SQLException {
        Connection connection = JdbcConnectionProvider.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sqlRequest);
        return stmt.executeQuery();
    }

    private void executeUpdateDB(String sqlRequest) throws SQLException{
        Connection connection = JdbcConnectionProvider.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sqlRequest);
        stmt.executeUpdate();
        connection.close();
    }
}