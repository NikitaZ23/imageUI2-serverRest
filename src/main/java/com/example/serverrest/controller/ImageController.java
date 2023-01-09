package com.example.serverrest.controller;

import com.example.serverrest.domain.Image;
import com.example.serverrest.dto.ImageDto;
import com.example.serverrest.dto.request.CreateImageRequest;
import com.example.serverrest.exceptions.ImageNotFoundExceptions;
import com.example.serverrest.exceptions.ImageNotFoundRestException;
import com.example.serverrest.mapper.ImageMapper;
import com.example.serverrest.service.Imp.ImageServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/images")
public class ImageController {
    public static final String IMAGE_NOT_FOUND = "Image not found";
    private final ImageServiceImp imageServiceImp;
    private final ImageMapper mapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<ImageDto> findAll() {
        return mapper.map(imageServiceImp.findAll());
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ImageDto createImage(@RequestParam("file") MultipartFile file) {
        return mapper.map(imageServiceImp.createImage(file));
    }

    @GetMapping("/{uuid}")
    @ResponseStatus(code = HttpStatus.OK)
    public ImageDto findByUUID(@PathVariable("uuid") final UUID uuid) {
        Image image = imageServiceImp.findByUuid(uuid).orElseThrow(() -> new ImageNotFoundRestException(IMAGE_NOT_FOUND));
        return mapper.map(image);
    }

    @GetMapping("/im/{name}")
    @ResponseStatus(code = HttpStatus.OK)
    public ImageDto findByName(@PathVariable("name") final String name) {
        Image image = imageServiceImp.findByName(name).orElseThrow(() -> new ImageNotFoundRestException(IMAGE_NOT_FOUND));
        return mapper.map(image);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ImageDto findById(@PathVariable("id") final int id) {
        Image image = imageServiceImp.findById(id).orElseThrow(() -> new ImageNotFoundRestException(IMAGE_NOT_FOUND));
        return mapper.map(image);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable("uuid") final UUID uuid) {
        try {
            imageServiceImp.deleteImage(uuid);
        } catch (ImageNotFoundExceptions e) {
            throw new ImageNotFoundRestException(e.getMessage());
        }
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(code = HttpStatus.OK)
    public ImageDto updateImage(@Valid @RequestBody final CreateImageRequest request,
                                @PathVariable("uuid") final UUID uuid) {
        Image image = imageServiceImp.updateImage(request, uuid).orElseThrow(() -> new ImageNotFoundRestException(IMAGE_NOT_FOUND));
        return mapper.map(image);
    }
}
