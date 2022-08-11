package com.awilkinson.TPXDemo.controller;

import com.awilkinson.TPXDemo.exception.InvalidURLException;
import com.awilkinson.TPXDemo.exception.URLNotFoundException;
import com.awilkinson.TPXDemo.model.ErrorResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {URLNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> urlNotFoundException(URLNotFoundException ex) {

        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), getStandardHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidURLException.class})
    public ResponseEntity<ErrorResponseDTO> invalidURLException(InvalidURLException ex) {

        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), getStandardHeaders(), HttpStatus.BAD_REQUEST);
    }

    private HttpHeaders getStandardHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
