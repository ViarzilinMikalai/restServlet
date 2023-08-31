package org.verzilin.servlet_api.dao;

import org.verzilin.servlet_api.domain.Post;

import java.util.List;

public interface PostDao {
    boolean savePost(Post post);
    boolean updatePost(Post post);
    Post getById(Long id);
    List<Post> getAllPost();
    boolean remove(Long id);
}
