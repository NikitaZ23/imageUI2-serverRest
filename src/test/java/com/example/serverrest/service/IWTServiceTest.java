package com.example.serverrest.service;

import com.example.serverrest.domain.ImWithTags;
import com.example.serverrest.domain.Tag;
import com.example.serverrest.dto.request.CreateIWTRequest;
import com.example.serverrest.exceptions.IWTNotFoundExceptions;
import com.example.serverrest.repository.ImWithTagsRepository;
import com.example.serverrest.service.Imp.ImWithTagsServiceImp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(MockitoExtension.class)
public class IWTServiceTest {
    @Mock
    ImWithTagsRepository repository;
    @InjectMocks
    ImWithTagsServiceImp service;

    @Test
    @DisplayName("Проверка получения пустого репозитория")
    public void findAllEmptyTest() {
        Iterable<ImWithTags> all = service.findAll();

        assertThat(all).isEmpty();
    }

    @Test
    @DisplayName("Проверка получения all репозитория")
    public void findAllTest() {
        ImWithTags iwt1 = new ImWithTags(1, new Tag("tag"));
        ImWithTags iwt2 = new ImWithTags(1, new Tag("tag2"));

        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(iwt1, iwt2));

        Iterable<ImWithTags> all = service.findAll();

        assertThat(all).isEqualTo(Arrays.asList(iwt1, iwt2));
    }

    @Test
    @DisplayName("Проверка получения тегов по id картинки")
    public void findAllTagTest() {
        Tag tag = new Tag("tag");
        Tag tag2 = new Tag("tag2");

        ImWithTags iwt1 = new ImWithTags(1, tag);
        ImWithTags iwt2 = new ImWithTags(1, tag2);

        Mockito.when(repository.findById_Im(Mockito.anyInt())).thenReturn(Arrays.asList(iwt1, iwt2));

        List<Tag> tags = service.getTags(1);

        assertThat(tags).isEqualTo(Arrays.asList(tag, tag2));
    }

    @Test
    @DisplayName("Проверка поиска по uuid")
    public void findByUuidTest() {
        ImWithTags iwt1 = new ImWithTags(1, new Tag("tag"));

        Mockito.when(repository.findByUuid(Mockito.any())).thenReturn(Optional.of(iwt1));

        Optional<ImWithTags> byUuid = service.findByUuid(UUID.randomUUID());

        assertThat(byUuid).isEqualTo(Optional.of(iwt1));
    }

    @Test
    @DisplayName("Проверка поиска по id картики и тега")
    public void findByOneObjectTest() {
        ImWithTags iwt1 = new ImWithTags(1, new Tag("tag"));

        Mockito.when(repository.findByOneObject(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Optional.of(iwt1));

        Optional<ImWithTags> byOneObject = service.findByOneObject(0, 0);

        assertThat(byOneObject).isEqualTo(Optional.of(iwt1));
    }

    @Test
    @DisplayName("Проверка поиска по id_im")
    public void findById_ImTest() {
        ImWithTags iwt1 = new ImWithTags(1, new Tag("tag"));
        ImWithTags iwt2 = new ImWithTags(1, new Tag("tag2"));
        ImWithTags iwt3 = new ImWithTags(1, new Tag("tag3"));

        Mockito.when(repository.findById_Im(Mockito.anyInt()))
                .thenReturn(Arrays.asList(iwt1, iwt2, iwt3));

        Iterable<ImWithTags> byIdIm = service.findById_Im(1);

        assertThat(byIdIm).isEqualTo(Arrays.asList(iwt1, iwt2, iwt3));
    }

    @Test
    @DisplayName("Проверка поиска по id_tg")
    public void findById_TgTest() {
        ImWithTags iwt1 = new ImWithTags(2, new Tag("tag"));
        ImWithTags iwt2 = new ImWithTags(3, new Tag("tag"));
        ImWithTags iwt3 = new ImWithTags(1, new Tag("tag"));

        Mockito.when(repository.findById_Tg(Mockito.anyInt()))
                .thenReturn(Arrays.asList(iwt1, iwt2, iwt3));

        Iterable<ImWithTags> byIdTg = service.findById_Tg(1);

        assertThat(byIdTg).isEqualTo(Arrays.asList(iwt1, iwt2, iwt3));
    }

    @Test
    @DisplayName("Проверка создания зависимости")
    public void createTest() {
        ImWithTags iwt1 = new ImWithTags(1, new Tag("tag"));

        Mockito.when(repository.save(Mockito.any())).thenReturn(iwt1);

        ImWithTags iwt2 = service.createIWT(new CreateIWTRequest(1, new Tag("tag")));

        assertThat(iwt2).isEqualTo(iwt1);
    }

    @Test
    @DisplayName("Проверка удаления зависимости")
    public void deleteTest() {
        ImWithTags iwt1 = new ImWithTags(1, new Tag("tag"));

        Mockito.when(repository.findByUuid(Mockito.any())).thenReturn(Optional.of(iwt1));

        service.delete(UUID.randomUUID());
        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка удаления зависимостей по коду картинки")
    public void deleteByImageTest() {
        ImWithTags iwt1 = new ImWithTags(1, new Tag("tag"));
        ImWithTags iwt2 = new ImWithTags(1, new Tag("tag2"));
        ImWithTags iwt3 = new ImWithTags(1, new Tag("tag3"));

        Mockito.when(repository.findById_Im(Mockito.anyInt()))
                .thenReturn(Arrays.asList(iwt1, iwt2, iwt3));

        service.deleteBy_IdIm(1);
        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка удаления зависимостей по коду тега")
    public void deleteByTagTest() {
        ImWithTags iwt1 = new ImWithTags(2, new Tag("tag"));
        ImWithTags iwt2 = new ImWithTags(3, new Tag("tag"));
        ImWithTags iwt3 = new ImWithTags(1, new Tag("tag"));

        Mockito.when(repository.findById_Tg(Mockito.anyInt()))
                .thenReturn(Arrays.asList(iwt1, iwt2, iwt3));

        service.deleteBy_IdTg(1);
        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка вызова ошибки при поиске по uuid зависимости")
    public void findByUuidTestWithException() {
        Mockito.when(repository.findByUuid(Mockito.any())).thenThrow(IWTNotFoundExceptions.class);

        Throwable throwable = catchThrowable(() -> service.findByUuid(UUID.randomUUID()));

        assertThat(throwable).isInstanceOf(IWTNotFoundExceptions.class);
    }
}
