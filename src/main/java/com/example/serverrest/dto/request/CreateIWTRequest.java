package com.example.serverrest.dto.request;

import com.example.serverrest.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateIWTRequest {
    int id_im;
    Tag id_tg;
}
