package com.test.driveanddeliver.infrastructure.repository;

import com.test.driveanddeliver.infrastructure.entity.DeliveryMethodEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface JpaDeliveryMethodRepository extends ReactiveCrudRepository<DeliveryMethodEntity, Long> {
    Mono<DeliveryMethodEntity> findByName(String drive);
}
