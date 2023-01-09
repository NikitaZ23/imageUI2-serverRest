package com.example.serverrest.service;

import com.example.serverrest.domain.Image;
import com.example.serverrest.dto.request.CreateImageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageService {
    Iterable<Image> findAll();

    Optional<Image> findByUuid(UUID uuid);

    Optional<Image> findById(int id);

    void deleteImage(UUID uuid);

    Image createImage(@NotNull CreateImageRequest request, List<String> list);

    Optional<Image> updateImage(@NotNull CreateImageRequest request, UUID uuid);

    Image createImage(@NotEmpty MultipartFile file);

    Optional<Image> findByName(String name);
}
