package com.test.driveanddeliver.application.dto;

import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public class DeliveryTimeSlotRequest {

    @NotNull
    private ZonedDateTime startTime;

    @NotNull
    private ZonedDateTime endTime;

    public @NotNull ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(@NotNull ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public @NotNull ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(@NotNull ZonedDateTime endTime) {
        this.endTime = endTime;
    }
}
