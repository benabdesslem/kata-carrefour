package com.test.driveanddeliver.domain.repository;

import com.test.driveanddeliver.domain.model.Booking;
import reactor.core.publisher.Mono;

public interface BookingRepository {
    Mono<Boolean> existsByUserIdAndDeliveryTimeSlotId(Long userId, Long deliveryTimeSlotId);

    Mono<Booking> save(Booking booking);
}
