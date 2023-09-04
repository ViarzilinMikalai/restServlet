package org.verzilin.servlet_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.factory.Mappers;
import org.verzilin.servlet_api.dao.UserDao;
import org.verzilin.servlet_api.domain.User;
import org.verzilin.servlet_api.dto.EasyUserDto;
import org.verzilin.servlet_api.dto.UserDto;
import org.verzilin.servlet_api.mapper.UserMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserService {
    UserDao userDao;
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    ObjectMapper objectMapper = new ObjectMapper();

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<String> getUsers(Long id) throws JsonProcessingException {
        Optional<String> request = Optional.empty();
        if (Objects.nonNull(id)) {
            User userFromDb = userDao.getById(id);
            if (Objects.nonNull(userFromDb)) {
                UserDto userDto = userMapper.userToUserDTO(userFromDb);
                request = Optional.of(objectMapper.writeValueAsString(userDto));
            }
        } else {
            List<User> users = userDao.getAllUsers();
            if (!users.isEmpty()) {
                List<EasyUserDto> usersDto = users
                        .stream()
                        .map(userMapper::toEasyUserDto)
                        .toList();
                request = Optional.of(objectMapper.writeValueAsString(usersDto));
            }
        }
        return request;
    }

    public boolean createUser(String requestBody) throws JsonProcessingException {
        UserDto userDto = objectMapper.readValue(requestBody, UserDto.class);
        return userDao.saveUser(userMapper.userDTOToUser(userDto));
    }

    public boolean updateUser(Long id, String requestBody) throws JsonProcessingException {
        boolean isUpdated = false;
        if (Objects.nonNull(id) && Objects.nonNull(userDao.getById(id))) {
            UserDto userDto = objectMapper.readValue(requestBody, UserDto.class);
            userDto.setId(id);
            isUpdated = userDao.updateUser(userMapper.userDTOToUser(userDto));
        }
        return isUpdated;
    }

    public boolean removeUser(Long id) {
        return userDao.remove(id);
    }
}
