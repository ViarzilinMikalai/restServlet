package org.verzilin.servlet_api.dao.impl;

import org.verzilin.servlet_api.config.JdbcConnectionProvider;
import org.verzilin.servlet_api.dao.SubscriptionDao;
import org.verzilin.servlet_api.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SubscriptionDaoImpl implements SubscriptionDao {
    Connection connection = JdbcConnectionProvider.getConnection();

    public SubscriptionDaoImpl() throws SQLException {
    }

    public boolean subscribe(Long ownerId, Long subscriberId) {
        int result = 0;
        String sqlSubscribe = "INSERT INTO subscription (owner_id, subscriber_id) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSubscribe)) {
            preparedStatement.setLong(1, ownerId);
            preparedStatement.setLong(2, subscriberId);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (result != 0);
    }

    public boolean unsubscribe(Long ownerId, Long subscriberId) {
        int result = 0;
        String sqlUnsubscribe = "DELETE FROM subscription WHERE owner_id = ? AND subscriber_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUnsubscribe)) {
            preparedStatement.setLong(1, ownerId);
            preparedStatement.setLong(2, subscriberId);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (result != 0);
    }

    @Override
    public Set<User> getSubscribers(Long owner) {
        String sqlSubscribers = "SELECT users.id, users.username FROM subscription JOIN users ON subscription.subscriber_id=users.id where subscription.owner_id = ?";

        Set<User> subscribers = new HashSet<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSubscribers)) {
            preparedStatement.setLong(1, owner);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                subscribers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscribers;
    }

    @Override
    public Set<User> getSubscriptions(Long subscriber) {
        String sqlSubscribers = "SELECT users.id, users.username FROM subscription JOIN users ON subscription.owner_id=users.id where subscription.subscriber_id = ?";

        Set<User> subscriptions = new HashSet<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSubscribers)) {
            preparedStatement.setLong(1, subscriber);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                subscriptions.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptions;
    }
}
