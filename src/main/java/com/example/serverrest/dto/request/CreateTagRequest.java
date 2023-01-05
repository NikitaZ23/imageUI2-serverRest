package com.example.serverrest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTagRequest {
    @NotBlank(message = "Name may not be empty")
    @Size(min = 1)
    String name;
}
