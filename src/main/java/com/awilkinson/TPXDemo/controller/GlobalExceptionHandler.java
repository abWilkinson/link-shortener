package com.awilkinson.TPXDemo.controller;

import com.awilkinson.TPXDemo.exception.InvalidURLException;
import com.awilkinson.TPXDemo.exception.URLNotFoundException;
import com.awilkinson.TPXDemo.model.ErrorResponseDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler
 *
 * Handle application exceptions which can occur, return an appropriate HttpStatus and message.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Catch URLNotFoundException and send the error message with http 404.
     * @param ex URLNotFoundException
     * @return An ErrorResponseDTO with message and 404 status
     */
    @ExceptionHandler(value = {URLNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> urlNotFoundException(URLNotFoundException ex) {

        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), getStandardHeaders(), HttpStatus.NOT_FOUND);
    }

    /**
     * Catch InvalidURLException and send the error message with http 400.
     * @param ex InvalidURLException
     * @return An ErrorResponseDTO with message and 400 status
     */
    @ExceptionHandler(value = {InvalidURLException.class})
    public ResponseEntity<ErrorResponseDTO> invalidURLException(InvalidURLException ex) {

        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), getStandardHeaders(), HttpStatus.BAD_REQUEST);
    }


    /**
     * Catch any exceptions or runtime exceptions and send a generic response with a http 500.
     * @param ex A Exception or RuntimeException
     * @return An ErrorResponseDTO with message and 500 status
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorResponseDTO> handleGenericExceptions(Throwable ex) {

        return new ResponseEntity<>(new ErrorResponseDTO("There has been a problem with your request, please try again later."),
                getStandardHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Override validation failures to provide a more user friendly error message.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errors = ex.getAllErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        return new ResponseEntity<>(new ErrorResponseDTO(errors), getStandardHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Add standard headers - currently only the content type.
     */
    private HttpHeaders getStandardHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
