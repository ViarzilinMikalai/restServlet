package org.verzilin.servlet_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.verzilin.servlet_api.domain.User;
import org.verzilin.servlet_api.dto.EasyUserDto;
import org.verzilin.servlet_api.dto.UserDto;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserDto userToUserDTO(User entity);

    @Mapping(target = "subscribers", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "posts", ignore = true)
    User userDTOToUser(UserDto dto);

    EasyUserDto toEasyUserDto(User user);

    User toFullUser(EasyUserDto easyUserDto);
}
