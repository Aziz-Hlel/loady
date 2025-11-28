package com.technoshark.Loady.ErrorHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.technoshark.Loady.ErrorHandler.Exceptions.ForbiddenAccessException;
import com.technoshark.Loady.ErrorHandler.Exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        // Handle Forbidden exceptions
        @ExceptionHandler(ForbiddenAccessException.class)
        public ResponseEntity<ErrorResponse> handleForbiddenAccess(
                        ForbiddenAccessException ex,
                        HttpServletRequest request) {
                return buildErrorResponse(
                                ex.getMessage() != null ? ex.getMessage() : "Access is forbidden",
                                null, // no field-level errors
                                HttpStatus.FORBIDDEN,
                                request);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                return buildErrorResponse(ex.getMessage(), null, HttpStatus.NOT_FOUND, request);
        }

        @ExceptionHandler(NoHandlerFoundException.class)
        public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex,
                        HttpServletRequest request) {
                return buildErrorResponse("API endpoint not found: " + ex.getRequestURL(), null, HttpStatus.NOT_FOUND,
                                request);
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex,
                        HttpServletRequest request) {
                return buildErrorResponse("API endpoint not found", null, HttpStatus.NOT_FOUND,
                                request);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleMalformedJson(HttpMessageNotReadableException ex,
                        HttpServletRequest request) {
                String message = "Malformed JSON request";
                if (ex.getCause() instanceof InvalidFormatException formatEx) {
                        message += ": Invalid value for field '" +
                                        formatEx.getPath().stream().findFirst().map(p -> p.getFieldName())
                                                        .orElse("unknown")
                                        +
                                        "'";
                }
                return buildErrorResponse(message, null, HttpStatus.BAD_REQUEST, request);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                return buildErrorResponse("Validation failed", errors, HttpStatus.BAD_REQUEST, request);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                        HttpServletRequest request) {
                String field = ex.getName();
                String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
                String value = String.valueOf(ex.getValue());

                Map<String, String> error = Map.of(field,
                                String.format("Invalid value '%s'. Expected type: %s", value, expected));
                return buildErrorResponse("Invalid request parameter", error, HttpStatus.BAD_REQUEST, request);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
                log.error("Unhandled exception: ", ex);
                return buildErrorResponse("An unexpected error occurred", null, HttpStatus.INTERNAL_SERVER_ERROR,
                                request);
        }

        // === Helper Method ===

        private ResponseEntity<ErrorResponse> buildErrorResponse(String message, Map<String, String> errors,
                        HttpStatus status, HttpServletRequest request) {
                ErrorResponse response = ErrorResponse.builder()
                                .message(message)
                                .path(request.getRequestURI())
                                .status(status.value())
                                .build();

                return ResponseEntity.status(status).body(response);
        }
}
