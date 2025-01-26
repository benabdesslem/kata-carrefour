package com.test.driveanddeliver.domain.repository;

import com.test.driveanddeliver.domain.model.DeliveryTimeSlot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeliveryTimeSlotRepository {
    Flux<DeliveryTimeSlot> findByDeliveryMethodId(Long deliveryMethodId);

    Mono<DeliveryTimeSlot> findById(Long deliveryTimeSlotId);

    Mono<DeliveryTimeSlot> save(DeliveryTimeSlot deliveryTimeSlot);
}