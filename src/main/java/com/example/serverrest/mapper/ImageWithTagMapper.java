package com.example.serverrest.mapper;

import com.example.serverrest.domain.ImWithTags;
import com.example.serverrest.dto.ImWithTagsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageWithTagMapper {
    @Mapping(source = "id_tg.id", target = "id_tg")
    ImWithTagsDto map(ImWithTags tag);

    Iterable<ImWithTagsDto> map(Iterable<ImWithTags> tags);
}
