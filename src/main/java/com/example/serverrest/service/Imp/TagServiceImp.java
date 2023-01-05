package com.example.serverrest.service.Imp;

import com.example.serverrest.domain.Tag;
import com.example.serverrest.dto.request.CreateTagRequest;
import com.example.serverrest.exceptions.TagExistsExceptions;
import com.example.serverrest.exceptions.TagNotFoundExceptions;
import com.example.serverrest.repository.TagRepository;
import com.example.serverrest.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TagServiceImp implements TagService {

    public static final String TAG_NOT_FOUND = "Tag not found";
    public static final String TAG_WITH_THIS_NAME_EXISTS = "tag with this name exists";
    private final TagRepository repository;

    @Override
    public Tag createTag(CreateTagRequest request) {
        Optional<Tag> tagOptional = repository.findByName(request.getName());
        return tagOptional.orElseGet(() -> repository.save(new Tag(request.getName())));
    }

    @Override
    public Iterable<Tag> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void deleteTag(UUID uuid) {
        Tag tag = findByUuid(uuid).orElseThrow(() -> new TagNotFoundExceptions(TAG_NOT_FOUND));
        repository.delete(tag);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Optional<Tag> findByUuid(UUID uuid) {
        return repository.findByUuid(uuid);
    }

    @Override
    @Transactional
    public Optional<Tag> updateTag(CreateTagRequest request, UUID uuid) {
        Optional<Tag> byName = findByName(request.getName());
        if (byName.isPresent()) {
            if (!byName.get().getUuid().equals(uuid))
                throw new TagExistsExceptions(TAG_WITH_THIS_NAME_EXISTS);
            else
                return byName;
        } else {
            Optional<Tag> byUuid = findByUuid(uuid);
            if (byUuid.isPresent()) {
                Tag tag = byUuid.get();
                tag.setName(request.getName());

                return Optional.of(repository.save(tag));
            } else
                return Optional.empty();
        }
    }

    @Override
    public Optional<Tag> findById(int id) {
        return repository.findById(id);
    }
}
