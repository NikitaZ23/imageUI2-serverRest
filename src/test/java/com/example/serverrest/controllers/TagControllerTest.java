package com.example.serverrest.controllers;

import com.example.serverrest.configuration.ConfigurationTests;
import com.example.serverrest.domain.Tag;
import com.example.serverrest.dto.TagDto;
import com.example.serverrest.dto.request.CreateTagRequest;
import com.example.serverrest.exceptions.TagExistsExceptions;
import com.example.serverrest.service.Imp.TagServiceImp;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = ConfigurationTests.class)
public class TagControllerTest {
    @MockBean
    TagServiceImp service;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @SneakyThrows
    @Test
    @DisplayName("Получение тега")
    public void getTagTest() {
        Tag tag = new Tag("tag");

        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.of(tag));

        mockMvc.perform(
                        get("/tags/7207d531-0e01-4cd0-ba0a-02f7c0c8fb2d")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("tag"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Получение тегов")
    public void getTagsTest() {
        Tag tag = new Tag("tag");
        Tag tag2 = new Tag("tag2");

        Mockito.when(service.findAll()).thenReturn(Arrays.asList(tag, tag2));

        mockMvc.perform(
                        get("/tags/")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(
                        new TagDto(tag.getId(), tag.getUuid(), tag.getName(), tag.getCreated(), tag.getModified()),
                        new TagDto(tag2.getId(), tag2.getUuid(), tag2.getName(), tag2.getCreated(), tag2.getModified())))));
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка создания тега")
    public void createTagTest() {
        Tag tag = new Tag("tag");

        CreateTagRequest request = new CreateTagRequest("tag");

        Mockito.when(service.createTag(Mockito.any())).thenReturn(tag);

        mockMvc.perform(
                        post("/tags")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("tag"));

    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка обновления тега")
    public void updateTagTest() {
        Tag tag = new Tag("tag");
        Tag tag2 = new Tag("tag2");

        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.of(tag));
        Mockito.when(service.updateTag(Mockito.any(), Mockito.any())).thenReturn(Optional.of(tag2));

        mockMvc.perform(
                        put("/tags/7207d531-0e01-4cd0-ba0a-02f7c0c8fb2d")
                                .content(objectMapper.writeValueAsString(new CreateTagRequest("Alex")))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("tag2"));
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка удаления тега")
    public void deleteTagTest() {
        Tag tag = new Tag("tag");

        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.of(tag));

        mockMvc.perform(
                        delete("/tags/" + tag.getUuid().toString()))
                .andExpect(status().isNoContent());

        System.out.println(tag);
    }

    @SneakyThrows
    @Test
    @DisplayName("Получение тега с ошибкой")
    public void getTagTestExceptions() {
        CreateTagRequest request = new CreateTagRequest("tag");

        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> mockMvc.perform(
                                get("/tags/7207d531-0e01-4cd0-ba0a-02f7c0c8fb2d")
                                        .content(objectMapper.writeValueAsString(request))
                                        .contentType(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk())
        );

        assertThat(thrown).isInstanceOf(AssertionError.class);
    }

    @Test
    @SneakyThrows
    @DisplayName("Проверка обновления тега с ошибкой")
    public void updateTagTestExceptions() {
        Tag tag = new Tag("tag");
        Mockito.when(service.findByUuid(Mockito.any())).thenReturn(Optional.of(tag));
        Mockito.when(service.updateTag(Mockito.any(), Mockito.any())).thenThrow(TagExistsExceptions.class);

        Throwable thrown = catchThrowable(() -> mockMvc.perform(
                        put("/tags/7207d531-0e01-4cd0-ba0a-02f7c0c8fb2d")
                                .content(objectMapper.writeValueAsString(new CreateTagRequest("tag")))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        );

        assertThat(thrown).isInstanceOf(AssertionError.class);
    }

}
