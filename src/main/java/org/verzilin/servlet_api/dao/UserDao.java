package org.verzilin.servlet_api.dao;

import org.verzilin.servlet_api.domain.User;

import java.util.List;

public interface UserDao {
    boolean saveUser(User user);
    boolean updateUser(User user);
    User getById(Long id);
    List<User> getAllUsers();
    boolean remove(Long id);
}
