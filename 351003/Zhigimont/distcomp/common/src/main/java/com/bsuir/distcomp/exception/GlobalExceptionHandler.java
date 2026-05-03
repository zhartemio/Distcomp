package com.bsuir.distcomp.exception;


import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.concurrent.TimeoutException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleAlreadyExists(EntityAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 403);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorResponse error = new ErrorResponse("Invalid numeric path/query parameter", 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleTimeout(TimeoutException ex) {
        ErrorResponse error = new ErrorResponse("Comment service timeout", 504);
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(error);
    }

}

