package org.verzilin.servlet_api.dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.verzilin.servlet_api.config.JdbcConnectionProvider;
import org.verzilin.servlet_api.domain.Post;
import org.verzilin.servlet_api.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {
    UserDaoImpl userDao = new UserDaoImpl();

    @Container
    PostgreSQLContainer psql;

    @BeforeEach
    void setUp() {
        psql = new PostgreSQLContainer("postgres:14.1-alpine")
                .withDatabaseName("foo")
                .withUsername("foo")
                .withPassword("secret");
        psql.start();
    }

    @AfterEach
    void tearDown() {
        psql.stop();
    }

    @Test
    void testSaveUser() throws SQLException{
        /**
         * Creating User for saving
         */
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        userDao.saveUser(user);

        /**
         * Reading saved post fom DB
         */
        String selectByUsername = "SELECT * FROM users WHERE username ='username'";
        ResultSet rs = executeQueryDB(selectByUsername);
        if (rs.next()) {
            assertEquals("username", rs.getString("username"));
            assertEquals("password", rs.getString("password"));
        }
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        userDao.saveUser(user);//save new user

        user.setId(1L);
        user.setPassword("newPassword");
        user.setUsername("newUsername");
        userDao.updateUser(user);//update new user

        User userFromDb = userDao.getById(1L);
        assertEquals("newUsername", userFromDb.getUsername());
        assertEquals("newPassword", userFromDb.getPassword());
    }

    @Test
    void testGetAllUsers() {
        userDao.saveUser(new User("username1", "password"));
        userDao.saveUser(new User("username2", "password"));
        userDao.saveUser(new User("username3", "password"));

        List<User> users = userDao.getAllUsers();
        assertTrue(users.size() == 3);
    }

    @Test
    void testGetById() throws SQLException {
        String createUser = "INSERT INTO users (username, password) VALUES ('username', 'password')";
        executeUpdateDB(createUser);
        User user = userDao.getById(2L);
        assertTrue(Objects.nonNull(user));
        assertTrue(user.getId() == 2);
        assertTrue(user.getPassword().equals("post1"));
    }

    @Test
    void testGetByIdWherePostNotFound() throws SQLException {
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
    }
}