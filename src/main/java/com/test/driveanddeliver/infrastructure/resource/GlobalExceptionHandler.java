package com.test.driveanddeliver.infrastructure.resource;

import com.test.driveanddeliver.domain.exception.AlreadyBookedException;
import com.test.driveanddeliver.domain.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public Mono<ResponseEntity<?>> handleBadCredentials(ServerWebExchange exchange, BadCredentialsException ex) {
        // Create a JSON response body with error details
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());

        // Return a ResponseEntity with JSON body and status code
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse));
    }


    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<?>> handleNotFoundException(ServerWebExchange exchange, NotFoundException ex) {
        // Create a JSON response body with error details
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getErrorKey(), ex.getReason());

        // Return a ResponseEntity with JSON body and status code
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse));
    }

    @ExceptionHandler(AlreadyBookedException.class)
    public Mono<ResponseEntity<?>> handleAlreadyBookedException(ServerWebExchange exchange, AlreadyBookedException ex) {
        // Create a JSON response body with error details
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, ex.getErrorKey(), ex.getReason());

        // Return a ResponseEntity with JSON body and status code
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse));
    }


    static class ErrorResponse {
        private int status;
        private String errorKey;
        private String message;

        public ErrorResponse(HttpStatus status, String message) {
            this.status = status.value();
            this.errorKey = status.getReasonPhrase();
            this.message = message;
        }


        public ErrorResponse(HttpStatus status, String errorKey, String message) {
            this.status = status.value();
            this.errorKey = status.getReasonPhrase();
            this.message = message;
        }


        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getErrorKey() {
            return errorKey;
        }

        public void setErrorKey(String errorKey) {
            this.errorKey = errorKey;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
