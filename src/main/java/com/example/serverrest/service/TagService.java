package com.example.serverrest.service;

import com.example.serverrest.domain.Tag;
import com.example.serverrest.dto.request.CreateTagRequest;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

public interface TagService {
    Tag createTag(@NotNull CreateTagRequest request);

    Iterable<Tag> findAll();

    void deleteTag(UUID uuid);

    Optional<Tag> findByUuid(UUID uuid);

    Optional<Tag> findById(int id);

    Optional<Tag> findByName(String name);

    Optional<Tag> updateTag(@NotNull CreateTagRequest request, UUID uuid);
}
