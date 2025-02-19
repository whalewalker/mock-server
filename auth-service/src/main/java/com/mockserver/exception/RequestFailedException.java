package com.mockserver.exception;


public class RequestFailedException extends RuntimeException {
    public RequestFailedException(String msg) {
        super(msg);
    }
}
