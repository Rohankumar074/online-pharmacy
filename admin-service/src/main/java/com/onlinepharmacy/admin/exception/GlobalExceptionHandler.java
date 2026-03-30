package com.onlinepharmacy.admin.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Global exception handler providing centralized error handling across the admin-service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();

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
   * Handles OpenFeign client exceptions thrown during inter-service communication.
   * Extracts the underlying microservice error message if available.
   *
   * @param ex the feign client exception
   * @param request the HTTP request
   * @return a structured error response with HTTP status matching the upstream error
   */
  @ExceptionHandler(FeignException.class)
  public ResponseEntity<ApiErrorResponse> handleFeignException(
      FeignException ex, HttpServletRequest request) {
    int status = ex.status();
    String message = ex.getMessage();

    // Try to extract the downstream error message from the response body
    try {
      String body = ex.contentUTF8();
      if (body != null && !body.isBlank()) {
        JsonNode json = objectMapper.readTree(body);
        if (json.has("message")) {
          message = json.get("message").asText();
        }
      }
    } catch (Exception ignored) {
      // Fall back to default Feign exception message
    }

    HttpStatusCode httpStatus = HttpStatusCode.valueOf(status > 0 ? status : 500);
    ApiErrorResponse response = new ApiErrorResponse(
        httpStatus.value(),
        HttpStatus.valueOf(httpStatus.value()).getReasonPhrase(),
        message,
        Instant.now(),
        request.getRequestURI()
    );
    return new ResponseEntity<>(response, httpStatus);
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
}
