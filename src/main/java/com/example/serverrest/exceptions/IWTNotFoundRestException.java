package com.example.serverrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IWTNotFoundRestException extends ResponseStatusException {
    public IWTNotFoundRestException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
