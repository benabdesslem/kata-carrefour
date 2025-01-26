package com.test.driveanddeliver.infrastructure.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;

@Table("booking")
public class BookingEntity {
    @Id
    private Long id;

    private ZonedDateTime bookingTime;

    private Long userId;

    private Long deliveryTimeSlotId;

    public BookingEntity() {
    }

    public BookingEntity(ZonedDateTime bookingTime, Long userId, Long deliveryTimeSlotId) {
        this.bookingTime = bookingTime;
        this.userId = userId;
        this.deliveryTimeSlotId = deliveryTimeSlotId;
    }

    public BookingEntity(Long id, ZonedDateTime bookingTime, Long userId, Long deliveryTimeSlotId) {
        this.id = id;
        this.bookingTime = bookingTime;
        this.userId = userId;
        this.deliveryTimeSlotId = deliveryTimeSlotId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(ZonedDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeliveryTimeSlotId() {
        return deliveryTimeSlotId;
    }

    public void setDeliveryTimeSlotId(Long deliveryTimeSlotId) {
        this.deliveryTimeSlotId = deliveryTimeSlotId;
    }
}
