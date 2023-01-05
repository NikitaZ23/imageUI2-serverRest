package com.example.serverrest.service.Imp;

import com.example.serverrest.domain.ImWithTags;
import com.example.serverrest.domain.Tag;
import com.example.serverrest.dto.request.CreateIWTRequest;
import com.example.serverrest.dto.request.CreateTagRequest;
import com.example.serverrest.exceptions.IWTNotFoundExceptions;
import com.example.serverrest.exceptions.ImageNotFoundExceptions;
import com.example.serverrest.repository.ImWithTagsRepository;
import com.example.serverrest.service.ImWithTagsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImWithTagsServiceImp implements ImWithTagsService {

    public static final String DEPENDENCY_NOT_FOUND = "Dependency Not Found";
    private final ImWithTagsRepository repository;

    TagServiceImp tagService;

    @Override
    @Transactional
    public void createIWT(int id_im, List<String> tags) {
        List<Tag> list = new ArrayList<>();

        tags.forEach(tag -> list.add(tagService.createTag(new CreateTagRequest(tag))));

        deleteBy_IdIm(id_im);
        System.out.println(list.size());
        System.out.println(list);
        list.forEach(tag -> createIWT(new CreateIWTRequest(id_im, tag)));
    }

    @Override
    @Transactional
    public void deleteBy_IdIm(int id_im) {
        Iterable<ImWithTags> imWithTags = repository.findById_Im(id_im);
        imWithTags.forEach(repository::delete);
    }

    @Override
    @Transactional
    public void deleteBy_IdTg(int id_tg) {
        Iterable<ImWithTags> imWithTags = repository.findById_Tg(id_tg);
        imWithTags.forEach(repository::delete);
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        ImWithTags imWithTag = repository.findByUuid(uuid).orElseThrow(() -> new ImageNotFoundExceptions(DEPENDENCY_NOT_FOUND));
        repository.delete(imWithTag);
    }

    @Override
    @Transactional
    public ImWithTags createIWT(CreateIWTRequest request) {
        Optional<ImWithTags> object = repository.findByOneObject(request.getId_im(), request.getId_tg().getId());
        return object.orElseGet(() -> repository.save(new ImWithTags(request.getId_im(), request.getId_tg())));
    }

    @Override
    public Iterable<ImWithTags> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ImWithTags> findByUuid(UUID id) {
        return repository.findByUuid(id);
    }

    @Override
    public List<Tag> getTags(int id_im) {
        List<Tag> tags = new ArrayList<>();
        findById_Im(id_im).forEach(imWithTags -> tags.add(imWithTags.getId_tg()));

        return tags;
    }

    @Override
    public Optional<ImWithTags> findByOneObject(int id_im, int id_tg) {
        ImWithTags im = repository.findByOneObject(id_im, id_tg).orElseThrow(() -> new IWTNotFoundExceptions(DEPENDENCY_NOT_FOUND));
        return Optional.of(im);
    }

    @Override
    public Iterable<ImWithTags> findById_Im(int id_im) {
        return repository.findById_Im(id_im);
    }

    @Override
    public Iterable<ImWithTags> findById_Tg(int id_tg) {
        return repository.findById_Tg(id_tg);
    }
}
