package com.test.driveanddeliver.application.port;

import com.test.driveanddeliver.application.dto.BookingRequest;
import com.test.driveanddeliver.application.dto.BookingResponse;
import com.test.driveanddeliver.application.dto.DeliveryTimeSlotRequest;
import com.test.driveanddeliver.domain.model.DeliveryMethod;
import com.test.driveanddeliver.domain.model.DeliveryTimeSlot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeliveryServicePort {
    Flux<DeliveryMethod> getAllDeliveryMethods();

    Flux<DeliveryTimeSlot> getDeliveryTimeSlotsByMethodId(Long deliveryMethodId);

    Mono<Void> bookDeliveryTimeSlot(Long deliveryTimeSlotId);

    Mono<DeliveryTimeSlot> addDeliveryTimeSlot(Long deliveryMethodId, DeliveryTimeSlotRequest request);

    Mono<BookingResponse> eventBooking(BookingRequest bookingRequest);
}
