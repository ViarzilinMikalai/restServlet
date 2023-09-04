package org.verzilin.servlet_api.dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
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
import static org.junit.jupiter.api.Assertions.assertEquals;


@Testcontainers
class PostDaoImplTest {
    private PostDaoImpl postDao = new PostDaoImpl();
    private UserDaoImpl userDao = new UserDaoImpl();

    @Container
    private PostgreSQLContainer psql = new PostgreSQLContainer("postgres:14.1-alpine")
        .withDatabaseName("foo")
        .withUsername("foo")
        .withPassword("secret");


    @Test
    void testSavePost() throws SQLException {
        /**
         * Saving User
         */
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        userDao.saveUser(user);
        user.setId(1L);

        Post post = new Post("Post title", "Post text", user);
        postDao.savePost(post); //saving Post

        /**
         * Reading saved post fom DB
         */
        String selectByTitle = "SELECT * FROM post WHERE title ='Post title'";
        ResultSet rs = executeQueryDB(selectByTitle);
        if (rs.next()) {
            assertEquals("Post title", rs.getString("title"));
            assertEquals("Post text", rs.getString("text"));
        }
    }

    @Test
    void testUpdatePost() {
        User user = new User();
        user.setId(1L);
        Post post = new Post(1L,"New post title", "New post text", user);
        postDao.updatePost(post);
        Post postFromBD = postDao.getById(1L);
        assertEquals("New post title", postFromBD.getTitle());
        assertEquals("New post text", postFromBD.getText());
    }

    @Test
    void testGetAllPost() {
        User user = new User(1L);
        postDao.savePost(new Post("post1", "test1", user));
        postDao.savePost(new Post("post2", "test2", user));
        postDao.savePost(new Post("post3", "test3", user));

        List<Post> posts = postDao.getAllPost();
        assertTrue(posts.size() == 7);
    }

    @Test
    void testGetById() throws SQLException {
        String createPost = "INSERT INTO post (title, text, author) VALUES ('Test title', 'Test text', '1')";
        executeUpdateDB(createPost);
        Post post = postDao.getById(2L);
        assertTrue(Objects.nonNull(post));
        assertTrue(post.getId() == 2);
        assertTrue(post.getTitle().equals("post2"));
    }

    @Test
    void testGetByIdWherePostNotFound() throws SQLException {
        String createPost = "INSERT INTO post (title, text, author) VALUES ('Test title', 'Test text', '1')";
        executeUpdateDB(createPost);
        Post post = postDao.getById(15L);
        assertTrue(Objects.isNull(post));
    }

    @Test
    void testRemove() {
        postDao.remove(1l);
        Post post = postDao.getById(1L);
        assertEquals(null, post);
    }

    @Test
    void testGetPostsByAuthorId() {
        User user = new User();
        user.setUsername("user");
        userDao.saveUser(user);
        user.setId(1L);
        postDao.savePost(new Post("post1", "test1", user));
        postDao.savePost(new Post("post2", "test2", user));
        postDao.savePost(new Post("post3", "test3", user));

        List<Post> posts = postDao.getPostsByAuthorId(1L);
        System.out.println(posts.size());
        assertTrue(posts.size() == 3);
    }

    /**
     * reading from DB
     * @param sqlRequest
     * @return
     * @throws SQLException
     */
    private ResultSet executeQueryDB(String sqlRequest) throws SQLException{
        Connection connection = JdbcConnectionProvider.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sqlRequest);
        return stmt.executeQuery();
    }

    /**
     * updateDB
     *
     * @param sqlRequest
     * @throws SQLException
     */
    private void executeUpdateDB(String sqlRequest) throws SQLException{
        Connection connection = JdbcConnectionProvider.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sqlRequest);
        stmt.executeUpdate();
    }
}