package com.awilkinson.TPXDemo.controller;

import com.awilkinson.TPXDemo.exception.InvalidURLException;
import com.awilkinson.TPXDemo.exception.URLNotFoundException;
import com.awilkinson.TPXDemo.model.ErrorResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    public void urlNotFoundExceptionReturnsCorrectly() {
        URLNotFoundException exception = new URLNotFoundException("A message");

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.urlNotFoundException(exception);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals("A message", response.getBody().getMsg());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    public void invalidURLExceptionReturnsCorrectly() {
        InvalidURLException exception = new InvalidURLException("A message");

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.invalidURLException(exception);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("A message", response.getBody().getMsg());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }
    
    @Test
    public void genericExceptionsReturnCorrectly() {
        RuntimeException exception = new RuntimeException("A message");

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleGenericExceptions(exception);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals("There has been a problem with your request, please try again later.", response.getBody().getMsg());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

}
