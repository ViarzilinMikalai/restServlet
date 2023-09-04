package org.verzilin.servlet_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.verzilin.servlet_api.dao.UserDao;
import org.verzilin.servlet_api.dao.impl.UserDaoImpl;
import org.verzilin.servlet_api.domain.Post;
import org.verzilin.servlet_api.domain.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class UserServiceTest {
    UserDao userDao = mock(UserDaoImpl.class);
    UserService userService = new UserService(userDao);

    @Test
    void getUsers() throws JsonProcessingException {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPassword("user2");

        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("title1");
        post1.setText("text1");
        post1.setAuthor(user1);

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("title2");
        post2.setText("text2");
        post2.setAuthor(user2);
        user2.setPosts(List.of(post2));

        Post post3 = new Post();
        post3.setId(3L);
        post3.setTitle("title3");
        post3.setText("text3");
        post3.setAuthor(user1);
        user1.setPosts(List.of(post1, post2));

        List<User> allUsers = new ArrayList<>(List.of(user1, user2));

        when(userDao.getById(1L)).thenReturn(user1);
        when(userDao.getAllUsers()).thenReturn(allUsers);

        userService.getUsers(1L).get();
        userService.getUsers(null).get();

        verify(userDao, times(1)).getById(1L);
        verify(userDao, times(1)).getAllUsers();
    }

    @Test
    void createUser() throws JsonProcessingException {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");

        Post post = new Post();
        post.setId(1L);
        post.setTitle("title");
        post.setText("text");
        post.setAuthor(user);
        user.setPosts(List.of(post));

        when(userDao.saveUser(user)).thenReturn(true);
        String request = "{\"id\":1,\"username\":\"user\",\"password\":null,\"subscribers\":[],\"subscriptions\":[]," +
                "\"posts\":[{\"id\":1,\"title\":\"title\",\"text\":\"text\",\"author\":{\"id\":1,\"username\":\"user\"}}]}";
        userService.createUser(request);

        verify(userDao, times(1)).saveUser(user);
    }

    @Test
    void updateUser() throws JsonProcessingException {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("password");

        Post post = new Post();
        post.setId(1L);
        post.setTitle("title");
        post.setText("text");
        post.setAuthor(user);
        user.setPosts(List.of(post));

        when(userDao.updateUser(user)).thenReturn(true);
        when(userDao.getById(1L)).thenReturn(user);
        String request = "{\"id\":1,\"username\":\"user2\",\"password\":null,\"subscribers\":[],\"subscriptions\":[]," +
                "\"posts\":[{\"id\":1,\"title\":\"title\",\"text\":\"text\",\"author\":{\"id\":1,\"username\":\"user2\"}}]}";
        userService.updateUser(1L, request);

        user.setUsername("user2");
        verify(userDao, times(1)).updateUser(user);
    }

    @Test
    void removeUser() {
        userService.removeUser(1L);
        verify(userDao, times(1)).remove(1L);
    }
}