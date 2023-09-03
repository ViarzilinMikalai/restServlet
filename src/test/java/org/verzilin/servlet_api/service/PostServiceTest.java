package org.verzilin.servlet_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.verzilin.servlet_api.dao.PostDao;
import org.verzilin.servlet_api.dao.impl.PostDaoImpl;
import org.verzilin.servlet_api.domain.Post;
import org.verzilin.servlet_api.domain.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


class PostServiceTest {
    PostDao postDao = mock(PostDaoImpl.class);
    PostService postService = new PostService(postDao);

    @Test
    void getPosts() throws JsonProcessingException {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

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

        Post post3 = new Post();
        post3.setId(3L);
        post3.setTitle("title3");
        post3.setText("text3");
        post3.setAuthor(user1);

        List<Post> allPost = new ArrayList<>();
        allPost.add(post1);
        allPost.add(post2);
        allPost.add(post2);

        when(postDao.getById(1L)).thenReturn(post1);
        when(postDao.getAllPost()).thenReturn(allPost);

        postService.getPosts(1L).get();
        postService.getPosts(null).get();

        verify(postDao, times(1)).getById(1L);
        verify(postDao, times(1)).getAllPost();
    }

    @Test
    void createPost() throws JsonProcessingException {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");

        Post post = new Post();
        post.setId(1L);
        post.setTitle("title");
        post.setText("text");
        post.setAuthor(user);
        user.setPosts(List.of(post));

        when(postDao.savePost(post)).thenReturn(true);
        String request = "{\"id\":1,\"title\":\"title\",\"text\":\"text\",\"author\":{\"id\":1,\"username\":\"user\"}}";
        postService.createPost(request);

        verify(postDao, times(1)).savePost(post);
    }

    @Test
    void updatePost() throws JsonProcessingException {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");

        Post post = new Post();
        post.setId(1L);
        post.setTitle("title");
        post.setText("text");
        post.setAuthor(user);
        user.setPosts(List.of(post));

        when(postDao.updatePost(post)).thenReturn(true);
        when(postDao.getById(1L)).thenReturn(post);
        String request = "{\"id\":1,\"title\":\"title22\",\"text\":\"text\",\"author\":{\"id\":1,\"username\":\"user\"}}";
        postService.updatePost(1L, request);

        post.setTitle("title22");
        verify(postDao, times(1)).updatePost(post);
    }

    @Test
    void removePost() {
        postService.removePost(1L);
        verify(postDao, times(1)).remove(1L);
    }
}