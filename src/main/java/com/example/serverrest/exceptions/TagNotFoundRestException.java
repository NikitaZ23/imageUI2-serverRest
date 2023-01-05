package com.example.serverrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TagNotFoundRestException extends ResponseStatusException {
    public TagNotFoundRestException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
