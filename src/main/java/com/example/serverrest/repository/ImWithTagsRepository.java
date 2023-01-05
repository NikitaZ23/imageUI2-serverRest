package com.example.serverrest.repository;

import com.example.serverrest.domain.ImWithTags;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImWithTagsRepository extends CrudRepository<ImWithTags, Integer> {
    @Query(value = "select * from imagewithtags where id_im = ?1", nativeQuery = true)
    Iterable<ImWithTags> findById_Im(int id_im);

    @Query(value = "select * from imagewithtags where id_tg = ?1", nativeQuery = true)
    Iterable<ImWithTags> findById_Tg(int id_tg);

    Optional<ImWithTags> findByUuid(UUID uuid);

    @Query(value = "select * from imagewithtags where id_im = ?1 and id_tg = ?2", nativeQuery = true)
    Optional<ImWithTags> findByOneObject(int id_im, int id_tg);

}
