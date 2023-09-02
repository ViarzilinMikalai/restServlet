package org.verzilin.servlet_api.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.verzilin.servlet_api.domain.Post;
import org.verzilin.servlet_api.domain.User;
import org.verzilin.servlet_api.dto.EasyUserDto;
import org.verzilin.servlet_api.dto.UserDto;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @Test
    void userToUserDTO() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user1");
        user2.setPassword("user1");

        Set<User> set1 = new HashSet<>();
        set1.add(user2);
        user1.setSubscribers(set1);
        user1.setSubscriptions(set1);

        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1L, "post", "text", user1));
        user1.setPosts(posts);

        UserDto userDto = userMapper.userToUserDTO(user1);
        assertEquals(user1.getUsername(), userDto.getUsername());
        assertEquals(null, userDto.getPassword());
        assertEquals(user1.getSubscribers().size(), userDto.getSubscribers().size());
        assertEquals(user1.getPosts().size(), userDto.getPosts().size());
    }

    @Test
    void userDTOToUser() {
        UserDto dto = new UserDto();
        dto.setId(1l);
        dto.setUsername("dto");
        dto.setPassword("password");

        User user = userMapper.userDTOToUser(dto);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getPassword(), user.getPassword());
    }

    @Test
    void toEasyUserDto() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("user");
        user.setPosts(Collections.emptyList());
        user.setSubscriptions(Collections.emptySet());
        user.setSubscribers(Collections.emptySet());

        EasyUserDto easyUserDto = userMapper.toEasyUserDto(user);
        assertEquals(easyUserDto.getId(), user.getId());
        assertEquals(easyUserDto.getUsername(), user.getUsername());
    }

    @Test
    void toFullUser() {
        EasyUserDto easyUserDto = new EasyUserDto();
        easyUserDto.setId(1L);
        easyUserDto.setUsername("easyUser");

        User user = userMapper.toFullUser(easyUserDto);
        assertEquals(easyUserDto.getId(), user.getId());
        assertEquals(easyUserDto.getUsername(), user.getUsername());
        assertEquals(null, user.getPassword());
        assertEquals(0, user.getPosts().size());
    }
}