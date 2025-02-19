package com.mockserver.exception;

public class RequestAlreadyPerformedException extends Exception{
    public RequestAlreadyPerformedException(String message) {
        super(message);
    }
}
