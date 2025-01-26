package com.test.driveanddeliver.infrastructure.repository;

import com.test.driveanddeliver.infrastructure.entity.BookingEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface JpaBookingRepository extends R2dbcRepository<BookingEntity, Long> {
    Mono<Boolean> existsByUserIdAndDeliveryTimeSlotId(Long userId, Long deliveryTimeSlotId);
}
