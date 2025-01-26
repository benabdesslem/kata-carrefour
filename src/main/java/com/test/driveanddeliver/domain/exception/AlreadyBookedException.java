package com.test.driveanddeliver.domain.exception;

public class AlreadyBookedException extends RuntimeException {
    private final String errorKey;
    private final String reason;

    public AlreadyBookedException(String errorKey, String reason) {
        super(reason);
        this.errorKey = errorKey;
        this.reason = reason;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public String getReason() {
        return reason;
    }
}
