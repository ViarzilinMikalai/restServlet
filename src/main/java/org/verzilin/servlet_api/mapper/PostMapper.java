package org.verzilin.servlet_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.verzilin.servlet_api.domain.Post;
import org.verzilin.servlet_api.dto.PostDto;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
    PostDto toPostDto(Post post);
    Post toPost(PostDto dto);

}
