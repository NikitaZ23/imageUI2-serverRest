package com.example.serverrest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ImWithTagsDto {
    int id;
    UUID uuid;
    int id_im;
    int id_tg;
}
