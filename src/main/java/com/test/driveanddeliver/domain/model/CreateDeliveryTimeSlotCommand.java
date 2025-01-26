package com.test.driveanddeliver.domain.model;


import java.time.ZonedDateTime;

public class CreateDeliveryTimeSlotCommand {

    public CreateDeliveryTimeSlotCommand() {
    }

    public CreateDeliveryTimeSlotCommand(ZonedDateTime startTime, ZonedDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }
}
