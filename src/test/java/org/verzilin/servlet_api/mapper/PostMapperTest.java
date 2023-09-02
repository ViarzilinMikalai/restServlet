package org.verzilin.servlet_api.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.verzilin.servlet_api.domain.Post;
import org.verzilin.servlet_api.domain.User;
import org.verzilin.servlet_api.dto.EasyUserDto;
import org.verzilin.servlet_api.dto.PostDto;

import static org.junit.jupiter.api.Assertions.*;

class PostMapperTest {
    PostMapper mapper = Mappers.getMapper(PostMapper.class);

    @Test
    void toPostDto() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Post");
        post.setText("New post");
        post.setAuthor(user);

        PostDto dto = mapper.toPostDto(post);
        assertEquals(post.getId(), dto.getId());
        assertEquals(post.getTitle(), dto.getTitle());
        assertEquals(post.getText(), dto.getText());
        assertEquals(post.getAuthor().getUsername(), dto.getAuthor().getUsername());
    }

    @Test
    void toPost() {
        EasyUserDto easyUserDto = new EasyUserDto();
        easyUserDto.setId(1L);
        easyUserDto.setUsername("user");

        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setTitle("Post");
        postDto.setText("New post");
        postDto.setAuthor(easyUserDto);

        Post post = mapper.toPost(postDto);
        assertEquals(postDto.getId(), post.getId());
        assertEquals(postDto.getTitle(), post.getTitle());
        assertEquals(postDto.getText(), post.getText());
        assertEquals(postDto.getAuthor().getUsername(), post.getAuthor().getUsername());
    }
}