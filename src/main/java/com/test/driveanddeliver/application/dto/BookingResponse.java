package com.test.driveanddeliver.application.dto;

public class BookingResponse {
    private Long bookingId;
    private boolean success;
    private String failureReason;

    public BookingResponse() {
    }

    public BookingResponse(boolean success, String failureReason) {
        this.success = success;
        this.failureReason = failureReason;
    }

    public BookingResponse(Long bookingId, boolean success) {
        this.bookingId = bookingId;
        this.success = success;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
