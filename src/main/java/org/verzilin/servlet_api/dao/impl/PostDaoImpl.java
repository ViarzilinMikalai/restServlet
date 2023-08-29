package org.verzilin.servlet_api.dao.impl;

import org.verzilin.servlet_api.config.JdbcConnectionProvider;
import org.verzilin.servlet_api.dao.PostDao;
import org.verzilin.servlet_api.domain.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PostDaoImpl implements PostDao {
    @Override
    public boolean savePost(Post post) {
        String sqlCreatePost = "INSERT INTO post (title, text, author) VALUES (?,?,?)";
        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement createPost = connection.prepareStatement(sqlCreatePost)) {
            createPost.setString(1, post.getTitle());
            createPost.setString(2, post.getText());
            createPost.setLong(3, post.getAuthor().getId());
            createPost.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean updatePost(Post post) {
        return false;
    }

    @Override
    public Post getById(Long id) {
//        String sqlCreatePost = "INSERT INTO post (title, text, author)" +
//                "VALUES (?,?,?)";
//        try (Connection connection = JdbcConnect.getConnection();
//             PreparedStatement createPost = connection.prepareStatement(sqlCreatePost)) {
//            createPost.setString(1, post.getTitle());
//            createPost.setString(2, post.getText());
//            createPost.setLong(3, post.getAuthor().getId());
//            createPost.executeUpdate();
//        } catch (SQLException e) {
//            e.getSQLState();
//        }
        return null;
    }

    @Override
    public List<Post> getAllPost() {
        return null;
    }

    @Override
    public List<Post> getPostByUsername(String username) {
        return null;
    }

    @Override
    public boolean remove(Long id) {
        return false;
    }
}
