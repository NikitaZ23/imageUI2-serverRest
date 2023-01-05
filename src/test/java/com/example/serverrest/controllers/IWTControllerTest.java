package com.example.serverrest.controllers;

import com.example.serverrest.configuration.ConfigurationTests;
import com.example.serverrest.domain.ImWithTags;
import com.example.serverrest.domain.Tag;
import com.example.serverrest.dto.ImWithTagsDto;
import com.example.serverrest.dto.request.CreateIWTRequest;
import com.example.serverrest.service.Imp.ImWithTagsServiceImp;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = ConfigurationTests.class)
public class IWTControllerTest {

    @MockBean
    ImWithTagsServiceImp service;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @SneakyThrows
    @Test
    @DisplayName("Получение зависимости")
    public void getIWTTest() {
        ImWithTags im = new ImWithTags(1, new Tag("tag"));

        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.of(im));

        mockMvc.perform(
                        get("/iwt/7207d531-0e01-4cd0-ba0a-02f7c0c8fb2d")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_im").value(1))
                .andExpect(jsonPath("$.id_tg").value(0));
    }

    @SneakyThrows
    @Test
    @DisplayName("Получение зависимостей")
    public void getTagsTest() {
        ImWithTags im = new ImWithTags(1, new Tag("tag"));
        ImWithTags im2 = new ImWithTags(1, new Tag("tag2"));

        Mockito.when(service.findAll()).thenReturn(Arrays.asList(im, im2));

        mockMvc.perform(
                        get("/iwt/")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(
                        new ImWithTagsDto(im.getId(), im.getId_im(), im.getId_tg().getId()),
                        new ImWithTagsDto(im2.getId(), im2.getId_im(), im2.getId_tg().getId())))));
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка создания зависимости")
    public void createTagTest() {
        ImWithTags im = new ImWithTags(1, new Tag("tag"));

        CreateIWTRequest request = new CreateIWTRequest(1, new Tag("tag"));

        Mockito.when(service.createIWT(Mockito.any())).thenReturn(im);

        mockMvc.perform(
                        post("/iwt")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_im").value(1))
                .andExpect(jsonPath("$.id_tg").value(0));
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка удаления зависимости")
    public void deleteIWTTest() {
        ImWithTags im = new ImWithTags(1, new Tag("tag"));

        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.of(im));

        mockMvc.perform(
                        delete("/iwt/" + im.getUuid().toString()))
                .andExpect(status().isNoContent());

        System.out.println(im);
    }

    @SneakyThrows
    @Test
    @DisplayName("Получение зависимости с ошибкой")
    public void getIWTTestExceptions() {
        CreateIWTRequest request = new CreateIWTRequest(1, new Tag("tag"));

        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> mockMvc.perform(
                                get("/iwt/7207d531-0e01-4cd0-ba0a-02f7c0c8fb2d")
                                        .content(objectMapper.writeValueAsString(request))
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
        );

        assertThat(thrown).isInstanceOf(AssertionError.class);
    }
}
