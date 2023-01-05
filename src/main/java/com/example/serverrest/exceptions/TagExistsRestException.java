package com.example.serverrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TagExistsRestException extends ResponseStatusException {
    public TagExistsRestException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
