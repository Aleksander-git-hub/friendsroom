package com.orion.friendsroom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException exception) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<?> handleJwtAuthenticationException(JwtAuthenticationException exception) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenError.class)
    public ResponseEntity<?> handleForbiddenError(ForbiddenError exception) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandling(Exception exception) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
