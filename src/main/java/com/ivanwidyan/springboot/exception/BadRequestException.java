package com.ivanwidyan.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {
    private static final long serialVersionUID = 1L;
    public BadRequestException(String message){
        super(message);
    }
}
