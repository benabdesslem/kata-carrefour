package com.test.driveanddeliver.domain.repository;

import com.test.driveanddeliver.domain.model.DeliveryMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeliveryMethodRepository {
    Flux<DeliveryMethod> findAll();

    Mono<DeliveryMethod> findById(Long deliveryMethodId);
}
