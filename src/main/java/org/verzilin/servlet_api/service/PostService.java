package org.verzilin.servlet_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.factory.Mappers;
import org.verzilin.servlet_api.dao.PostDao;
import org.verzilin.servlet_api.domain.Post;
import org.verzilin.servlet_api.dto.PostDto;
import org.verzilin.servlet_api.mapper.PostMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PostService {
    PostDao postDao;
    PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    ObjectMapper objectMapper = new ObjectMapper();

    public PostService(PostDao postDao) {
        this.postDao = postDao;
    }

    public Optional<String> getPosts(Long id) throws JsonProcessingException {
        Optional<String> request = Optional.empty();
        if (Objects.nonNull(id)) {
            Post postFromDb = postDao.getById(id);
            if (Objects.nonNull(postFromDb)) {
                PostDto postDto = postMapper.toPostDto(postFromDb);
                request = Optional.of(objectMapper.writeValueAsString(postDto));
            }
        } else {
            List<Post> posts = postDao.getAllPost();
            if (!posts.isEmpty()) {
                List<PostDto> postDto = posts
                        .stream()
                        .map(postMapper::toPostDto)
                        .toList();
                request = Optional.of(objectMapper.writeValueAsString(postDto));
            }
        }
        return request;
    }

    public boolean createPost(String requestBody) throws JsonProcessingException {
        PostDto postDto = objectMapper.readValue(requestBody, PostDto.class);
        return postDao.savePost(postMapper.toPost(postDto));
    }

    public boolean updatePost(Long id, String requestBody) throws JsonProcessingException {
        boolean isUpdated = false;
        if (Objects.nonNull(id) && Objects.nonNull(postDao.getById(id))) {
            PostDto postDto = objectMapper.readValue(requestBody, PostDto.class);
            postDto.setId(id);
            isUpdated = postDao.updatePost(postMapper.toPost(postDto));
        }
        return isUpdated;
    }

    public boolean removePost(Long id) {
        return postDao.remove(id);
    }
}
