package com.onlinepharmacy.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Global exception handler providing centralized error handling across the order-service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles validation exceptions thrown by @Valid arguments.
   *
   * @param ex the validation exception
   * @param request the HTTP request
   * @return a structured error response with HTTP 400 Bad Request
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    String message = ex.getBindingResult().getAllErrors().stream()
        .map(error -> {
          String fieldName = ((FieldError) error).getField();
          String errorMessage = error.getDefaultMessage();
          return fieldName + ": " + errorMessage;
        })
        .collect(Collectors.joining("; "));

    ApiErrorResponse response = new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        message,
        Instant.now(),
        request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles illegal argument exceptions.
   *
   * @param ex the illegal argument exception
   * @param request the HTTP request
   * @return a structured error response with HTTP 400 Bad Request
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex, HttpServletRequest request) {
    ApiErrorResponse response = new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        ex.getMessage(),
        Instant.now(),
        request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles generic unexpected exceptions.
   *
   * @param ex the general exception
   * @param request the HTTP request
   * @return a structured error response with HTTP 500 Internal Server Error
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGlobalException(
      Exception ex, HttpServletRequest request) {
    ApiErrorResponse response = new ApiErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        "An unexpected error occurred: " + ex.getMessage(),
        Instant.now(),
        request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles resource not found exceptions.
   *
   * @param ex the not found exception
   * @param request the HTTP request
   * @return a structured error response with HTTP 404 Not Found
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex, HttpServletRequest request) {
    ApiErrorResponse response = new ApiErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage(),
        Instant.now(),
        request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles forbidden access exceptions.
   *
   * @param ex the forbidden exception
   * @param request the HTTP request
   * @return a structured error response with HTTP 403 Forbidden
   */
  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ApiErrorResponse> handleForbiddenException(
      ForbiddenException ex, HttpServletRequest request) {
    ApiErrorResponse response = new ApiErrorResponse(
        HttpStatus.FORBIDDEN.value(),
        HttpStatus.FORBIDDEN.getReasonPhrase(),
        ex.getMessage(),
        Instant.now(),
        request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  /**
   * Handles business logic conflict exceptions.
   *
   * @param ex the conflict exception
   * @param request the HTTP request
   * @return a structured error response with HTTP 409 Conflict
   */
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ApiErrorResponse> handleConflictException(
      ConflictException ex, HttpServletRequest request) {
    ApiErrorResponse response = new ApiErrorResponse(
        HttpStatus.CONFLICT.value(),
        HttpStatus.CONFLICT.getReasonPhrase(),
        ex.getMessage(),
        Instant.now(),
        request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
  }
}
