package com.mockserver.controller.advice;


import com.mockserver.exception.*;
import com.mockserver.exception.ResourceNotFoundException;
import com.mockserver.model.enums.ResponseCode;
import com.mockserver.model.shared.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.mockserver.model.shared.Error;


import java.io.IOException;
import java.util.List;

@org.springframework.web.bind.annotation.ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@ResponseBody
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Response<?>> handleDuplicateKeyException(DuplicateKeyException e) {
        return commonResponseForDuplicateError(e.getMessage(), e);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return commonResponseForBadRequest("Data integrity violation", e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleValidationException(MethodArgumentNotValidException e) {
        List<Error> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new Error(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return new ResponseEntity<>(new Response<>(ResponseCode.BAD_REQUEST.code, "Bad request", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleBindingException(BindException e) {
        return commonResponseForBadRequest("Invalid url parameters", e);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return commonResponseForBadRequest("Required input file is missing", e);
    }

    @ExceptionHandler(RequestFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response<?>> handleRequestFailedException(RequestFailedException e) {
        return commonResponseForSystemError(e.getMessage(), e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response<?>> handleException(Exception e) {
        return commonResponseForSystemError("Error occurred, please contact the administrator", e);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Response<?>> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(new Response<>(ResponseCode.UNAUTHORIZED.code, e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Response<?>> handleBadCredentialsException(BadCredentialsException e) {
        return commonResponseForUnauthorized(e.getMessage(), e);
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Response<?>> handleDisabledException(DisabledException e) {
        return commonResponseForUnauthorized(e.getMessage(), e);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return commonResponseForBadRequest(e.getMessage(), e);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return commonResponseForBadRequest("Invalid url", e);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Response<?>> handleResourceAlreadyExistException(ResourceAlreadyExistException e) {
        return commonResponseForDuplicateError(e.getMessage(), e);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<?>> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new Response<>(ResponseCode.NOT_FOUND.code, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Response<?>> handleIOException(IOException e) {
        return new ResponseEntity<>(new Response<>(ResponseCode.BAD_REQUEST.code, e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(RequestIsBadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleRequestIsBadException(RequestIsBadException e) {
        if (StringUtils.isBlank(e.getFieldName())) {
            return commonResponseForBadRequest(e.getMessage(), e);
        } else {
            Error error = new Error(e.getFieldName(), e.getFieldMessage());
            return new ResponseEntity<>(new Response<>(ResponseCode.BAD_REQUEST.code, ResponseCode.BAD_REQUEST.message, List.of(error)), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ResponseEntity<Response<?>> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException e) {
        return new ResponseEntity<>(new Response<>(ResponseCode.SYSTEM_ERROR.code, "Request timed out"), HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(RequestAlreadyPerformedException.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Response<?>> handleRequestAlreadyPerformedException(RequestAlreadyPerformedException e) {
        return new ResponseEntity<>(new Response<>(ResponseCode.SUCCESSFUL.code, e.getMessage()), HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return commonResponseForBadRequest(e.getMessage(), e);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return commonResponseForBadRequest("Request not supported", e);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return commonResponseForBadRequest("Json Parse Error", e);
    }

    private ResponseEntity<Response<?>> commonResponse(String message, Exception e, ResponseCode responseCode, HttpStatus status) {
        log.error(message, e);
        return new ResponseEntity<>(new Response<>(responseCode.code, message), status);
    }

    private ResponseEntity<Response<?>> commonResponseForDuplicateError(String message, Exception e) {
        return commonResponse(message, e, ResponseCode.DUPLICATE_REQUEST, HttpStatus.CONFLICT);
    }

    private ResponseEntity<Response<?>> commonResponseForBadRequest(String message, Exception e) {
        return commonResponse(message, e, ResponseCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Response<?>> commonResponseForSystemError(String message, Exception e) {
        return commonResponse(message, e, ResponseCode.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Response<?>> commonResponseForUnauthorized(String message, Exception e) {
        return commonResponse(message, e, ResponseCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }
}