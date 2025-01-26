package com.test.driveanddeliver.infrastructure.adapter;

import com.test.driveanddeliver.domain.model.Booking;
import com.test.driveanddeliver.domain.repository.BookingRepository;
import com.test.driveanddeliver.infrastructure.entity.BookingEntity;
import com.test.driveanddeliver.infrastructure.repository.JpaBookingRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BookingRepositoryAdapter implements BookingRepository {

    private final JpaBookingRepository jpaRepository;

    public BookingRepositoryAdapter(JpaBookingRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public Mono<Boolean> existsByUserIdAndDeliveryTimeSlotId(Long userId, Long deliveryTimeSlotId) {

        return jpaRepository.existsByUserIdAndDeliveryTimeSlotId(userId, deliveryTimeSlotId);
    }

    @Override
    public Mono<Booking> save(Booking booking) {
        return jpaRepository.save(toEntity(booking))
                .map(this::toDomain);
    }

    private BookingEntity toEntity(Booking booking) {
        return new BookingEntity(booking.getId(), booking.getBookingTime(), booking.getUserId(), booking.getDeliveryTimeSlotId());
    }

    private Booking toDomain(BookingEntity entity) {
        return new Booking(entity.getId(), entity.getBookingTime(), entity.getUserId(), entity.getDeliveryTimeSlotId());
    }
}
