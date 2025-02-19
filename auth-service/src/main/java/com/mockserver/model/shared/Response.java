package com.mockserver.model.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {
    private String statusCode;
    private String message;
    private List<Error> errors;
    private T data;
    private LocalDateTime timestamp;

    public Response(String statusCode, String message, List<Error> errors) {
        this(statusCode, message);
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public Response(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
