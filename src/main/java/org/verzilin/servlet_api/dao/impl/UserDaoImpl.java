package org.verzilin.servlet_api.dao.impl;

import org.verzilin.servlet_api.config.JdbcConnectionProvider;
import org.verzilin.servlet_api.dao.UserDao;
import org.verzilin.servlet_api.domain.Post;
import org.verzilin.servlet_api.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean saveUser(User user) {
        int result = 0;
        String sqlCreateUser = "INSERT INTO user (username, password) VALUES (?,?)";
        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement createUser = connection.prepareStatement(sqlCreateUser)) {
            createUser.setString(1, user.getUsername());
            createUser.setString(2, user.getPassword());
            result = createUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (result != 0);
    }

    @Override
    public boolean updateUser(User user) {
        int result = 0;
        String sqlUpdateUser = "UPDATE user SET  username = ? , password = ? WHERE id = ?";
        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement updateUser = connection.prepareStatement(sqlUpdateUser)) {
            updateUser.setString(1, user.getUsername());
            updateUser.setString(2, user.getPassword());
            updateUser.setLong(3, user.getId());
            result = updateUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (result != 0);
    }

    @Override
    public User getById(Long id) {
        String sqlGetPostById = "SELECT * FROM post WHERE id = ?";
        User user = null;

        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement getPost = connection.prepareStatement(sqlGetPostById)) {
            getPost.setLong(1, id);
            ResultSet rs = getPost.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlGetAllUser = "SELECT * FROM user";
        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement getUsers = connection.prepareStatement(sqlGetAllUser)) {
            ResultSet rs = getUsers.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getLong("id"), rs.getString("username"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean remove(Long id) {
        int result = 0;
        String sqlDeleteUser = "DELETE FROM user WHERE id = ?";
        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement deleteUser = connection.prepareStatement(sqlDeleteUser)) {
            deleteUser.setLong(1, id);
            result = deleteUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (result != 0);
    }
}
