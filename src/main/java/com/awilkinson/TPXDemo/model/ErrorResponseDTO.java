package com.awilkinson.TPXDemo.model;

public class ErrorResponseDTO {
    private final String msg;

    public ErrorResponseDTO(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
