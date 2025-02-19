package com.mockserver.model.enums;

public enum ResponseCode {
    SUCCESSFUL("00", "Successful"),
    BAD_REQUEST("02", "Bad request"),
    DUPLICATE_REQUEST("03", "Entity already exist"),
    NOT_FOUND("04", "Entity doesn't exist"),
    SYSTEM_ERROR("96", "Internal System Error, Please try again later."),
    UNAUTHORIZED("01", "Unauthorized");

    public final String code;
    public final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}