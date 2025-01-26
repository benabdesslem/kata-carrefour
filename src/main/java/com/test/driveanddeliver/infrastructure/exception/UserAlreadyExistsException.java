package com.test.driveanddeliver.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CustomErrorException {

    public UserAlreadyExistsException(String reason) {
        super(HttpStatus.BAD_REQUEST, "USER_ALREADY_EXISTS", reason);
    }

    public UserAlreadyExistsException(String reason, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, "USER_ALREADY_EXISTS", reason, cause);
    }
}
