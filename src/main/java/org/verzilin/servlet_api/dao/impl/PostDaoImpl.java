package org.verzilin.servlet_api.dao.impl;

import org.verzilin.servlet_api.config.JdbcConnectionProvider;
import org.verzilin.servlet_api.dao.PostDao;
import org.verzilin.servlet_api.domain.Post;
import org.verzilin.servlet_api.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostDaoImpl implements PostDao {
    @Override
    public boolean savePost(Post post) {
        int result = 0;
        String sqlCreatePost = "INSERT INTO post (title, text, author) VALUES (?,?,?)";
        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement createPost = connection.prepareStatement(sqlCreatePost)) {
            createPost.setString(1, post.getTitle());
            createPost.setString(2, post.getText());
            createPost.setLong(3, post.getAuthor().getId());
            result = createPost.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (result == 0) ? false : true;
    }

    @Override
    public boolean updatePost(Post post) {
        return false;
    }

    @Override
    public Post getById(Long id) {
        String sqlGetPostById = "SELECT * FROM post WHERE id = ?";
        Post post = null;

        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement getPost = connection.prepareStatement(sqlGetPostById)) {
            getPost.setLong(1, id);
            ResultSet rs = getPost.executeQuery();
            if (rs.next()) {
                post = new Post();
                post.setId(rs.getLong("id"));
                post.setTitle(rs.getString("title"));
                post.setText(rs.getString("text"));
                post.setAuthor(new User());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public List<Post> getAllPost() {
        String sqlGetAllPost = "SELECT * FROM post";
        ArrayList<Post> posts = new ArrayList<>();

        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement getPost = connection.prepareStatement(sqlGetAllPost)) {
            ResultSet rs = getPost.executeQuery();
            while (rs.next()) {
                Post post = new Post(rs.getLong("id"), rs.getString("title"), rs.getString("text"), new User());
                posts.add(post);
                rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public boolean remove(Long id) {
        return false;
    }
}
