package com.awilkinson.TPXDemo.model;

/**
 * A standard response DTO in case of an error, which contains a user appropriate error message.
 */
public class ErrorResponseDTO {
    private final String msg;

    public ErrorResponseDTO(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
