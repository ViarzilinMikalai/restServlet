package org.verzilin.servlet_api.dao.impl;

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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Testcontainers
public class PostDaoImplTest {
    PostDaoImpl postDao = new PostDaoImpl();

    @Container
    PostgreSQLContainer psql = new PostgreSQLContainer("postgres:14.1-alpine")
            .withDatabaseName("foo")
            .withUsername("foo")
            .withPassword("secret");

    @Test
    public void testSavePost() throws SQLException {
        /**
         * Creating Post for saving
         */
        psql.start();
        User user = new User();
        user.setId(1L);
        Post post = new Post("Post title", "Post text", user);
        postDao.savePost(post); //saving Post

        /**
         * Reading saved post fom DB
         */
        String selectByTitle = "SELECT * FROM post WHERE title ='Post title'";
        ResultSet rs = readFromDB(selectByTitle);
        if (rs.next()) {
            assertEquals("Post title", rs.getString("title"));
            assertEquals("Post text", rs.getString("text"));
        }

    }



//        try {
//
//            Connection conn = JdbcConnect.getConnection();
//            String createTable = "CREATE TABLE post (id INTEGER AUTO_INCREMENT, title VARCHAR(255), text VARCHAR(2000), PRIMARY KEY(id))";
//            PreparedStatement stmt = conn.prepareStatement(createTable);
//            stmt.execute();
//
//            String insertData = "INSERT INTO Users (name) VALUES ('John')";
//            stmt = conn.prepareStatement(insertData);
//            stmt.execute();
//
//            String selectAll = "SELECT * FROM Users";
//            stmt = conn.prepareStatement(selectAll);
//            ResultSet rs = stmt.executeQuery();
//
//            rs.next();
//            assertEquals(1, rs.getInt("id"));
//            assertEquals("John", rs.getString("name"));
//        } catch (Exception e) {
//
//        }
//    }

    @Test
    void updatePost() {
    }

    /**
     reading from DB
     */
    private ResultSet readFromDB(String sqlRequest) throws SQLException{
        Connection connection = JdbcConnectionProvider.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sqlRequest);
        return stmt.executeQuery();
    }
}