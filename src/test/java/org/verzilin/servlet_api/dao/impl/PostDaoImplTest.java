package org.verzilin.servlet_api.dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.verzilin.servlet_api.config.JdbcConnectionProvider;
import org.verzilin.servlet_api.dao.PostDao;
import org.verzilin.servlet_api.dao.UserDao;
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
    private PostDao postDao = new PostDaoImpl();
    private UserDao userDao = new UserDaoImpl();

    @Container
    private PostgreSQLContainer psql = new PostgreSQLContainer("postgres:14.1-alpine")
        .withDatabaseName("foo")
        .withUsername("foo")
        .withPassword("secret");

    PostDaoImplTest() throws SQLException {
    }

    User user = new User();

    {
        // Save User
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("password");
        userDao.saveUser(user);
    }

    @AfterEach
    void clear() throws SQLException {
        executeUpdateDB("DELETE FROM post;TRUNCATE TABLE post RESTART IDENTITY CASCADE;");
    }

    @Test
    void testSavePost() throws SQLException {
        Post post = new Post("Post title", "Post text", user);
        postDao.savePost(post); //saving Post

        //Reading saved post fom DB
        String selectByTitle = "SELECT * FROM post WHERE title ='Post title'";
        ResultSet rs = executeQueryDB(selectByTitle);
        if (rs.next()) {
            assertEquals("Post title", rs.getString("title"));
            assertEquals("Post text", rs.getString("text"));
        }
    }

    @Test
    void testUpdatePost() {
        // Create and save new Post
        Post post = new Post("Post title", "Post text", user);
        postDao.savePost(post); //saving Post

        post.setId(1L);
        post.setTitle("New post title");
        post.setText("New post text");
        postDao.updatePost(post);

        Post postFromBD = postDao.getById(1L);
        assertEquals("New post title", postFromBD.getTitle());
        assertEquals("New post text", postFromBD.getText());
        assertEquals(post, postFromBD);
    }

    @Test
    void testGetAllPost() {
        postDao.savePost(new Post("post1", "test1", user));
        postDao.savePost(new Post("post2", "test2", user));
        postDao.savePost(new Post("post3", "test3", user));

        List<Post> posts = postDao.getAllPost();
        assertTrue(posts.size() == 3);
    }

    @Test
    void testGetById() throws SQLException {
        String createPost = "INSERT INTO post (title, text, author) VALUES ('Test title', 'Test text', '1')";
        executeUpdateDB(createPost);

        Post post = postDao.getById(1L);
        assertTrue(Objects.nonNull(post));
        assertTrue(post.getAuthor().getId() == 1);
        assertEquals("Test title", post.getTitle());
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
        Post post = new Post(1L,"post1", "test1", user);
        postDao.savePost(post);

        Post postFromDb = postDao.getById(1L);
        assertEquals(post, postFromDb);

        postDao.remove(1l);
        assertEquals(null, postDao.getById(1L));
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
     * @param sqlRequest
     * @throws SQLException
     */
    private void executeUpdateDB(String sqlRequest) throws SQLException{
        Connection connection = JdbcConnectionProvider.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sqlRequest);
        stmt.executeUpdate();
    }
}