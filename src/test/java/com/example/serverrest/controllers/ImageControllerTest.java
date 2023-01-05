package com.example.serverrest.controllers;

import com.example.serverrest.configuration.ConfigurationTests;
import com.example.serverrest.domain.Image;
import com.example.serverrest.dto.ImageDto;
import com.example.serverrest.dto.request.CreateImageRequest;
import com.example.serverrest.service.Imp.ImageServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = ConfigurationTests.class)
public class ImageControllerTest {
    @MockBean
    ImageServiceImp service;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @SneakyThrows
    @Test
    @DisplayName("Получение картинки")
    public void getImageTest() {
        Image image = new Image("name");

        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.of(image));

        mockMvc.perform(
                        get("/images/7207d531-0e01-4cd0-ba0a-02f7c0c8fb2d")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Получение картинок")
    public void getImagesTest() {
        Image image = new Image("1");
        Image image2 = new Image("2");

        Mockito.when(service.findAll()).thenReturn(Arrays.asList(image, image2));

        mockMvc.perform(
                        get("/images/")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(
                        new ImageDto(image.getId(), image.getUuid(), image.getName(), image.getCreated(), image.getModified()),
                        new ImageDto(image2.getId(), image2.getUuid(), image2.getName(), image2.getCreated(), image2.getModified())))));
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка создания картинки")
    public void createImageTest() {
        MockMultipartFile file
                = new MockMultipartFile(
                "hello.txt",
                "hello.txt",
                "text/plain",
                "Hello, World!".getBytes()
        );

        MockMvc mockMvc
                = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/images/").file(file))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка обновления картинки")
    public void updateImageTest() {
        Image image = new Image("1");
        Image image2 = new Image("2");

        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.of(image));
        Mockito.when(service.updateImage(Mockito.any(), Mockito.any())).thenReturn(Optional.of(image2));

        mockMvc.perform(
                        put("/images/7207d531-0e01-4cd0-ba0a-02f7c0c8fb2d")
                                .content(objectMapper.writeValueAsString(new CreateImageRequest("2")))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("2"));
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка удаления картинки")
    public void deleteImageTest() {
        Image image = new Image("1");

        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.of(image));

        mockMvc.perform(
                        delete("/images/" + image.getUuid().toString()))
                .andExpect(status().isNoContent());

        System.out.println(image);
    }

    @SneakyThrows
    @Test
    @DisplayName("Получение картинки с ошибкой")
    public void getImageTestExceptions() {
        CreateImageRequest request = new CreateImageRequest("1");

        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> mockMvc.perform(
                                get("/images/7207d531-0e01-4cd0-ba0a-02f7c0c8fb2d")
                                        .content(objectMapper.writeValueAsString(request))
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
        );

        assertThat(thrown).isInstanceOf(AssertionError.class);
    }
}
