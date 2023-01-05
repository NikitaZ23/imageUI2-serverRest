package com.example.serverrest.service;

import com.example.serverrest.domain.Tag;
import com.example.serverrest.dto.request.CreateTagRequest;
import com.example.serverrest.exceptions.TagExistsExceptions;
import com.example.serverrest.exceptions.TagNotFoundExceptions;
import com.example.serverrest.repository.TagRepository;
import com.example.serverrest.service.Imp.TagServiceImp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @Mock
    TagRepository repository;

    @InjectMocks
    TagServiceImp serviceImp;

    @Test
    @DisplayName("Проверка получения пустого репозитория")
    public void findAllEmptyTest() {
        Iterable<Tag> all = serviceImp.findAll();

        assertThat(all).isEmpty();
    }

    @Test
    @DisplayName("Проверка получения всех тегов")
    public void findAllTest() {
        Tag tag = new Tag("tag1");
        Tag tag2 = new Tag("tag2");

        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(tag, tag2));

        Iterable<Tag> all = serviceImp.findAll();

        assertThat(Arrays.asList(tag, tag2)).isEqualTo(all);
    }

    @Test
    @DisplayName("Проверка получения тега по uuid")
    public void findTagTest() {
        Tag tag = new Tag("tag");

        Mockito.when(repository.findByUuid(Mockito.any())).thenReturn(Optional.of(tag));

        Optional<Tag> byUuid = serviceImp.findByUuid(UUID.randomUUID());

        assertThat(byUuid).isEqualTo(Optional.of(tag));
    }

    @Test
    @DisplayName("Проверка получения тега по id")
    public void findTagIdTest() {
        Tag tag = new Tag("tag");

        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(tag));

        Optional<Tag> byUuid = serviceImp.findById(1);

        assertThat(byUuid).isEqualTo(Optional.of(tag));
    }

    @Test
    @DisplayName("Проверка получения тега по имени")
    public void findTagNameTest() {
        Tag tag = new Tag("tag");

        Mockito.when(repository.findByName(Mockito.any())).thenReturn(Optional.of(tag));

        Optional<Tag> byUuid = serviceImp.findByName("tag");

        assertThat(byUuid).isEqualTo(Optional.of(tag));
    }

    @Test
    @DisplayName("Проверка создания тега")
    public void createTagTest() {
        Tag tag = new Tag("tag");

        Mockito.when(repository.save(Mockito.any())).thenReturn(tag);

        Tag byUuid = serviceImp.createTag(new CreateTagRequest("tag"));

        assertThat(byUuid).isEqualTo(tag);
    }

    @Test
    @DisplayName("Проверка обновления тега")
    public void updateTagTest() {
        Tag tag = new Tag("tag");
        Tag tag2 = new Tag("tag2");

        Mockito.when(repository.save(Mockito.any())).thenReturn(tag2);
        Mockito.when(repository.findByName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(repository.findByUuid(Mockito.any())).thenReturn(Optional.of(tag));

        Optional<Tag> byUuid = serviceImp.updateTag(new CreateTagRequest("tag2"), UUID.randomUUID());

        assertThat(byUuid).isEqualTo(Optional.of(tag2));
    }

    @Test
    @DisplayName("Проверка удаления тега")
    public void deleteTagTest() {
        Tag tag = new Tag("tag");

        Mockito.when(repository.findByUuid(Mockito.any())).thenReturn(Optional.of(tag));

        serviceImp.deleteTag(UUID.randomUUID());

        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка вызова ошибки при поиске по uuid тега")
    public void findByUuidTestWithException() {
        Mockito.when(repository.findByUuid(Mockito.any())).thenThrow(TagNotFoundExceptions.class);

        Throwable throwable = catchThrowable(() -> serviceImp.findByUuid(UUID.randomUUID()));

        assertThat(throwable).isInstanceOf(TagNotFoundExceptions.class);
    }

    @Test
    @DisplayName("Проверка вызова ошибки при обновлении тега")
    public void updateTagExceptionTest() {
        Tag tag2 = new Tag("tag2");

        Mockito.when(repository.findByName(Mockito.any())).thenReturn(Optional.of(tag2));

        Throwable throwable = catchThrowable(() -> serviceImp.updateTag(new CreateTagRequest("tag2"), UUID.randomUUID()));

        assertThat(throwable).isInstanceOf(TagExistsExceptions.class);
    }
}
