package com.example.serverrest.controller;

import com.example.serverrest.domain.Tag;
import com.example.serverrest.dto.TagDto;
import com.example.serverrest.dto.request.CreateTagRequest;
import com.example.serverrest.exceptions.TagExistsExceptions;
import com.example.serverrest.exceptions.TagExistsRestException;
import com.example.serverrest.exceptions.TagNotFoundExceptions;
import com.example.serverrest.exceptions.TagNotFoundRestException;
import com.example.serverrest.mapper.TagMapper;
import com.example.serverrest.service.Imp.TagServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/tags")
public class TagController {
    public static final String TAG_NOT_FOUND = "Tag not found";
    private final TagServiceImp tagServiceImp;

    private final TagMapper mapper;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public TagDto createTag(@Valid @RequestBody final CreateTagRequest request) {
        return mapper.map(tagServiceImp.createTag(request));
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<TagDto> getAll() {
        return mapper.map(tagServiceImp.findAll());
    }

    @DeleteMapping("/{tagId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable("tagId") final UUID uuid) {
        try {
            tagServiceImp.deleteTag(uuid);
        } catch (TagNotFoundExceptions e) {
            throw new TagNotFoundRestException(TAG_NOT_FOUND);
        }
    }

    @GetMapping("/{tagId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TagDto findTag(@PathVariable("tagId") final UUID uuid) {
        Tag tag = tagServiceImp.findByUuid(uuid).orElseThrow(() -> new TagNotFoundRestException(TAG_NOT_FOUND));
        return mapper.map(tag);
    }

    @PutMapping("/{tagId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TagDto updateTag(@Valid @RequestBody final CreateTagRequest request,
                            @PathVariable("tagId") final UUID uuid) {
        try {
            Tag tag = tagServiceImp.updateTag(request, uuid).orElseThrow(() -> new TagNotFoundRestException(TAG_NOT_FOUND));
            return mapper.map(tag);
        } catch (TagExistsExceptions e) {
            throw new TagExistsRestException(e.getMessage());
        }
    }

}
