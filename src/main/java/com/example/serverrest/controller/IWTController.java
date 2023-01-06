package com.example.serverrest.controller;

import com.example.serverrest.domain.ImWithTags;
import com.example.serverrest.dto.ImWithTagsDto;
import com.example.serverrest.dto.ImageDto;
import com.example.serverrest.dto.TagDto;
import com.example.serverrest.dto.request.CreateIWTRequest;
import com.example.serverrest.dto.request.CreateImageRequest;
import com.example.serverrest.dto.request.FindIWTRequest;
import com.example.serverrest.exceptions.IWTNotFoundRestException;
import com.example.serverrest.exceptions.ImageNotFoundExceptions;
import com.example.serverrest.exceptions.ImageNotFoundRestException;
import com.example.serverrest.mapper.ImageWithTagMapper;
import com.example.serverrest.service.Imp.ImWithTagsServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/iwt")
@AllArgsConstructor
public class IWTController {
    public static final String DEPENDENCE_NOT_FOUND = "Dependence not found";
    private final ImageWithTagMapper mapper;

    private final ImWithTagsServiceImp serviceImp;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<ImWithTagsDto> findAll() {
        return mapper.map(serviceImp.findAll());
    }

    @GetMapping("/{uuid}")
    @ResponseStatus(code = HttpStatus.OK)
    public ImWithTagsDto findImageTags(@PathVariable("uuid") final UUID id) {
        ImWithTags imWithTags = serviceImp.findByUuid(id).orElseThrow(() -> new IWTNotFoundRestException(DEPENDENCE_NOT_FOUND));
        return mapper.map(imWithTags);
    }
    @GetMapping("/im/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<ImWithTagsDto> findTags(@PathVariable("id") final int imageId){
        return mapper.map(serviceImp.findById_Im(imageId));
    }

    @GetMapping("/tg/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<ImWithTagsDto> findImages(@PathVariable("id") final int tagId){
        return mapper.map(serviceImp.findById_Tg(tagId));
    }

    @GetMapping("/oneOb")
    @ResponseStatus(code = HttpStatus.OK)
    public ImWithTagsDto findByOneObject(@Valid @RequestBody final FindIWTRequest request){
        System.out.println(request);
        ImWithTags imWithTags = serviceImp.findByOneObject(request.getId_im(), request.getId_tg()).orElseThrow(() -> new IWTNotFoundRestException(DEPENDENCE_NOT_FOUND));
        System.out.println(imWithTags);
        return mapper.map(imWithTags);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ImWithTagsDto createDependence(@Valid @RequestBody final CreateIWTRequest request) {
        return mapper.map(serviceImp.createIWT(request));
    }

    @PostMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void createDependences(@Valid @RequestBody final List<String> list,
                                @PathVariable("id") final int id) {
        serviceImp.createIWT(id, list);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteDependence(@PathVariable("uuid") final UUID uuid) {
        try {
            serviceImp.delete(uuid);
        } catch (ImageNotFoundExceptions e) {
            throw new ImageNotFoundRestException(e.getMessage());
        }
    }


}
