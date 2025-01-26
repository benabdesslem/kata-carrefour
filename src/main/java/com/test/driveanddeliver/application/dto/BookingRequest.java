package com.test.driveanddeliver.application.dto;

public class BookingRequest {
    private Long timeSlotId;
    private Long userId;

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "BookingRequest{" +
                "timeSlotId=" + timeSlotId +
                ", userId=" + userId +
                '}';
    }
}