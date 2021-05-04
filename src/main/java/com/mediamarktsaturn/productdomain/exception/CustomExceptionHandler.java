package com.mediamarktsaturn.productdomain.exception;

import com.mediamarktsaturn.productdomain.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleCategoryNotFoundException(
            final CategoryNotFoundException ex) {
        final ErrorResponse errorDetails = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryDeleteException.class)
    public final ResponseEntity<ErrorResponse> handleCategoryDeleteException(
            final CategoryDeleteException ex) {
        final ErrorResponse errorDetails = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryInvalidException.class)
    public final ResponseEntity<ErrorResponse> handleCategoryInvalidException(
            final CategoryInvalidException ex) {
        final ErrorResponse errorDetails = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleProductNotFoundException(
            final ProductNotFoundException ex) {
        final ErrorResponse errorDetails = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductInvalidException.class)
    public final ResponseEntity<ErrorResponse> handleProductInvalidException(
            final ProductInvalidException ex) {
        final ErrorResponse errorDetails = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllOtherExceptions(
            final Exception ex) {
        final ErrorResponse errorDetails = new ErrorResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
