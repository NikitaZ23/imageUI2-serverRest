package com.example.serverrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ImageNotFoundRestException extends ResponseStatusException {
    public ImageNotFoundRestException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
