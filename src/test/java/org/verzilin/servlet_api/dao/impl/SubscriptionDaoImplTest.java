package org.verzilin.servlet_api.dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.verzilin.servlet_api.config.JdbcConnectionProvider;
import org.verzilin.servlet_api.dao.SubscriptionDao;
import org.verzilin.servlet_api.dao.UserDao;
import org.verzilin.servlet_api.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionDaoImplTest {
    private UserDao userDao = new UserDaoImpl();
    private SubscriptionDao subscriptionDao = new SubscriptionDaoImpl();

    SubscriptionDaoImplTest() throws SQLException {
    }

    @Container
    private PostgreSQLContainer psql = new PostgreSQLContainer("postgres:14.1-alpine")
        .withDatabaseName("foo")
        .withUsername("foo")
        .withPassword("secret");


    User user1 = new User();
    User user2 = new User();
    User user3 = new User();
    User user4 = new User();

    {
        user1.setId(1L);
        user1.setUsername("user1");

        user2.setId(2L);
        user2.setUsername("user2");

        user3.setId(3L);
        user3.setUsername("user3");

        user4.setId(4L);
        user4.setUsername("user4");

        userDao.saveUser(user1);
        userDao.saveUser(user2);
        userDao.saveUser(user3);
        userDao.saveUser(user4);
    }

    @AfterEach
    void clear() throws SQLException {
        String sqlRequest = "DELETE FROM subscription;";
        Connection connection = JdbcConnectionProvider.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sqlRequest);
        stmt.executeUpdate();
    }

    @Test
    void subscribe() throws SQLException {
        subscriptionDao.subscribe(user1.getId(), user2.getId());

        String selectByOwnerId = "SELECT users.id, users.username FROM subscription JOIN users ON subscription.subscriber_id=users.id where subscription.owner_id = 1";
        ResultSet rs = executeQueryDB(selectByOwnerId);
        if (rs.next()) {
            assertEquals("user2", rs.getString("username"));
        }
    }

    @Test
    void unsubscribe() throws SQLException {
        subscriptionDao.subscribe(user2.getId(), user1.getId());
        subscriptionDao.unsubscribe(user2.getId(), user1.getId());

        String selectByOwnerId = "SELECT users.id, users.username FROM subscription JOIN users ON subscription.subscriber_id=users.id where subscription.owner_id = 2";
        ResultSet rs = executeQueryDB(selectByOwnerId);
        assertEquals(false, rs.next());
    }

    @Test
    void getSubscribers() {
        subscriptionDao.subscribe(user1.getId(), user2.getId());
        subscriptionDao.subscribe(user1.getId(), user3.getId());
        subscriptionDao.subscribe(user1.getId(), user4.getId());

        Set<User> subscribers = subscriptionDao.getSubscribers(user1.getId());
        assertEquals(3, subscribers.size());
    }

    @Test
    void getSubscriptions() {
        subscriptionDao.subscribe(user1.getId(), user2.getId());
        subscriptionDao.subscribe(user3.getId(), user2.getId());
        subscriptionDao.subscribe(user4.getId(), user2.getId());

        Set<User> subscriptions = subscriptionDao.getSubscriptions(user2.getId());
        assertEquals(3, subscriptions.size());
    }

    private ResultSet executeQueryDB(String sqlRequest) throws SQLException{
        Connection connection = JdbcConnectionProvider.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sqlRequest);
        return stmt.executeQuery();
    }
}