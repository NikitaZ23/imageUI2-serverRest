package com.example.serverrest.mapper;

import com.example.serverrest.domain.Tag;
import com.example.serverrest.dto.TagDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto map(Tag tag);

    Iterable<TagDto> map(Iterable<Tag> tags);
}
