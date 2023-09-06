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

    public PostDaoImpl() {
    }

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
        return (result != 0);
    }

    @Override
    public boolean updatePost(Post post) {
        int result = 0;
        String sqlUpdatePost = "UPDATE post SET  title = ? , text = ? WHERE id = ?";
        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement updatePost = connection.prepareStatement(sqlUpdatePost)) {
            updatePost.setString(1, post.getTitle());
            updatePost.setString(2, post.getText());
            updatePost.setLong(3, post.getId());
            result = updatePost.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (result != 0);
    }

    @Override
    public Post getById(Long id) {
        String sqlGetPostById = "SELECT post.id, post.title, post.text, post.author, users.username  FROM post JOIN users ON post.author=users.id WHERE post.id = ? ";
        Post post = null;
        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement getPost = connection.prepareStatement(sqlGetPostById)) {
            getPost.setLong(1, id);
            ResultSet rs = getPost.executeQuery();
            if (rs.next()) {
                User user = new User();
                post = new Post();
                post.setId(rs.getLong("id"));
                post.setTitle(rs.getString("title"));
                post.setText(rs.getString("text"));
                user.setId(rs.getLong("author"));
                user.setUsername(rs.getString("username"));
                post.setAuthor(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public List<Post> getAllPost() {
        String sqlGetAllPost = "SELECT post.id, post.title, post.text, post.author, users.username  FROM post JOIN users ON post.author=users.id";
        ArrayList<Post> posts = new ArrayList<>();

        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement getPost = connection.prepareStatement(sqlGetAllPost)) {
            ResultSet rs = getPost.executeQuery();
            while (rs.next()) {
                User user = new User();
                Post post = new Post();
                post.setId(rs.getLong("id"));
                post.setTitle(rs.getString("title"));
                post.setText(rs.getString("text"));
                user.setId(rs.getLong("author"));
                user.setUsername(rs.getString("username"));
                post.setAuthor(user);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public boolean remove(Long id) {
        int result = 0;
        String sqlDeletePost = "DELETE FROM post WHERE id = ?";
        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement deletePost = connection.prepareStatement(sqlDeletePost)) {
            deletePost.setLong(1, id);
            result = deletePost.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (result != 0);
    }

    @Override
    public List<Post> getPostsByAuthorId(Long id) {
        String sqlGetAllPost = "SELECT post.id, post.title, post.text, post.author, users.username  FROM post JOIN users ON post.author=users.id where post.author = ?";
        ArrayList<Post> posts = new ArrayList<>();

        try (Connection connection = JdbcConnectionProvider.getConnection();
             PreparedStatement getPost = connection.prepareStatement(sqlGetAllPost)) {
            getPost.setLong(1, id);
            ResultSet rs = getPost.executeQuery();
            while (rs.next()) {
                User user = new User();
                Post post = new Post();
                post.setId(rs.getLong("id"));
                post.setTitle(rs.getString("title"));
                post.setText(rs.getString("text"));
                user.setId(rs.getLong("author"));
                user.setUsername(rs.getString("username"));
                post.setAuthor(user);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
}
