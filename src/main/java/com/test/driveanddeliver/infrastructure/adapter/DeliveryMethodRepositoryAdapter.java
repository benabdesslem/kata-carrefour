package com.test.driveanddeliver.infrastructure.adapter;

import com.test.driveanddeliver.domain.model.DeliveryMethod;
import com.test.driveanddeliver.domain.repository.DeliveryMethodRepository;
import com.test.driveanddeliver.infrastructure.entity.DeliveryMethodEntity;
import com.test.driveanddeliver.infrastructure.repository.JpaDeliveryMethodRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DeliveryMethodRepositoryAdapter implements DeliveryMethodRepository {

    private final JpaDeliveryMethodRepository jpaRepository;

    public DeliveryMethodRepositoryAdapter(JpaDeliveryMethodRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Flux<DeliveryMethod> findAll() {
        return jpaRepository.findAll()
                .map(this::toDomain);
    }

    @Override
    public Mono<DeliveryMethod> findById(Long deliveryMethodId) {
        return jpaRepository.findById(deliveryMethodId)
                .map(this::toDomain);
    }

    private DeliveryMethod toDomain(DeliveryMethodEntity entity) {
        return new DeliveryMethod(entity.getId(), entity.getName());
    }

    private DeliveryMethodEntity toEntity(DeliveryMethod deliveryMethod) {
        return new DeliveryMethodEntity(deliveryMethod.getId(), deliveryMethod.getName());
    }
}
