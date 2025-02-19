package com.mockserver.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RemoteServerException extends RuntimeException {
    private final String message;

    public RemoteServerException(String message) {
        this.message = message;
    }
}
