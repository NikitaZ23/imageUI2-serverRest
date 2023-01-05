package com.example.serverrest.mapper;

import com.example.serverrest.domain.Image;
import com.example.serverrest.dto.ImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDto map(Image image);

    Iterable<ImageDto> map(Iterable<Image> images);
}
