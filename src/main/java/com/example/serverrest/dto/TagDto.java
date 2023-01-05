package com.example.serverrest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TagDto {
    int id;
    UUID uuid;
    String name;
    LocalDateTime created;
    LocalDateTime modified;
}
