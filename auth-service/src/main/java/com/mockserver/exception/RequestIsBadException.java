package com.mockserver.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class RequestIsBadException extends RuntimeException {
    private final String fieldName;
    private final String fieldMessage;

    public RequestIsBadException(String msg) {
        super(msg);
        this.fieldName = "";
        this.fieldMessage = msg;
    }

    public RequestIsBadException(String fieldName, String fieldMessage) {
        super(fieldMessage);
        this.fieldName = fieldName;
        this.fieldMessage = fieldMessage;
    }
}
