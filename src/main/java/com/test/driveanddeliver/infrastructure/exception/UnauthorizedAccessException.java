package com.test.driveanddeliver.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedAccessException extends CustomErrorException {

    public UnauthorizedAccessException(String errorKey, String reason) {
        super(HttpStatus.UNAUTHORIZED, errorKey, reason);
    }

    public UnauthorizedAccessException(String errorKey, String reason, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, errorKey, reason, cause);
    }
}