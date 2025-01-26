package com.test.driveanddeliver.infrastructure.repository;

import com.test.driveanddeliver.infrastructure.entity.DeliveryTimeSlotEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface JpaDeliveryTimeSlotRepository extends ReactiveCrudRepository<DeliveryTimeSlotEntity, Long> {
    Flux<DeliveryTimeSlotEntity> findByDeliveryMethodId(Long deliveryMethodId);
}