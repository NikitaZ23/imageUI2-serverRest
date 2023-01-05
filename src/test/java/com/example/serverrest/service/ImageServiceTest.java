package com.example.serverrest.service;

import com.example.serverrest.domain.Image;
import com.example.serverrest.dto.request.CreateImageRequest;
import com.example.serverrest.repository.ImagesRepository;
import com.example.serverrest.service.Imp.ImWithTagsServiceImp;
import com.example.serverrest.service.Imp.ImageServiceImp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    ImagesRepository repository;

    @InjectMocks
    ImageServiceImp service;
    @Mock
    ImWithTagsServiceImp serviceImp;


    @Test
    @DisplayName("Проверка получения пустого репозитория")
    public void findAllEmptyTest() {
        Iterable<Image> all = service.findAll();

        assertThat(all).isEmpty();
    }

    @Test
    @DisplayName("Проверка получения всех картинок")
    public void findAllTest() {
        Image image = new Image("a");
        Image image2 = new Image("b");

        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(image, image2));

        Iterable<Image> all = service.findAll();

        assertThat(Arrays.asList(image, image2)).isEqualTo(all);
    }

    @Test
    @DisplayName("Проверка получения картинки по uuid")
    public void findImageTest() {
        Image image = new Image("a");

        Mockito.when(repository.findByUuid(Mockito.any())).thenReturn(Optional.of(image));

        Optional<Image> byUuid = service.findByUuid(UUID.randomUUID());

        assertThat(byUuid).isEqualTo(Optional.of(image));
    }

    @Test
    @DisplayName("Проверка получения картинки по id")
    public void findImageIdTest() {
        Image image = new Image("a");

        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(image));

        Optional<Image> byUuid = service.findById(0);

        assertThat(byUuid).isEqualTo(Optional.of(image));
    }

    @Test
    @DisplayName("Проверка получения картинки по имени")
    public void findImageNameTest() {
        Image image = new Image("a");

        Mockito.when(repository.findByName(Mockito.anyString())).thenReturn(Optional.of(image));

        Optional<Image> byUuid = service.findByName("1");

        assertThat(byUuid).isEqualTo(Optional.of(image));
    }

    @Test
    @DisplayName("Проверка удаления картинки")
    public void deleteImageTest() {
        Image image = new Image("a");

        Mockito.when(repository.findByUuid(Mockito.any())).thenReturn(Optional.of(image));

        service.deleteImage(UUID.randomUUID());

        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка обновления картинки")
    public void updateImageTest() {
        Image image = new Image("1");
        Image image2 = new Image("2");

        Mockito.when(repository.save(Mockito.any())).thenReturn(image2);
        Mockito.when(repository.findByUuid(Mockito.any())).thenReturn(Optional.of(image));

        Optional<Image> image3 = service.updateImage(new CreateImageRequest("2"), UUID.randomUUID());

        assertThat(image3).isEqualTo(Optional.of(image2));
    }

    @Test
    @DisplayName("Проверка создания картинки")
    public void createImage() {
        Image image = new Image("1");

        List<String> list = new ArrayList<>();
        list.add("tag1");

        Mockito.when(repository.save(Mockito.any())).thenReturn(image);

        Image image2 = service.createImage(new CreateImageRequest("1"), list);

        assertThat(image).isEqualTo(image2);
    }
}
