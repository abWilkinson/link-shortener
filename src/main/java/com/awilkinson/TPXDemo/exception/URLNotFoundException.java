package com.awilkinson.TPXDemo.exception;

public class URLNotFoundException extends RuntimeException {

    public URLNotFoundException(String msg) {
        super(msg);
    }
}