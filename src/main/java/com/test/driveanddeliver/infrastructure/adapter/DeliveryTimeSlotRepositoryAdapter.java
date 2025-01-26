package com.test.driveanddeliver.infrastructure.adapter;

import com.test.driveanddeliver.domain.model.DeliveryTimeSlot;
import com.test.driveanddeliver.domain.repository.DeliveryTimeSlotRepository;
import com.test.driveanddeliver.infrastructure.entity.DeliveryTimeSlotEntity;
import com.test.driveanddeliver.infrastructure.repository.JpaDeliveryTimeSlotRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DeliveryTimeSlotRepositoryAdapter implements DeliveryTimeSlotRepository {

    private final JpaDeliveryTimeSlotRepository jpaRepository;

    public DeliveryTimeSlotRepositoryAdapter(JpaDeliveryTimeSlotRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Flux<DeliveryTimeSlot> findByDeliveryMethodId(Long deliveryMethodId) {
        return jpaRepository.findByDeliveryMethodId(deliveryMethodId)
                .map(this::toDomain);
    }

    @Override
    public Mono<DeliveryTimeSlot> findById(Long deliveryTimeSlotId) {
        return jpaRepository.findById(deliveryTimeSlotId)
                .map(this::toDomain);
    }

    @Override
    public Mono<DeliveryTimeSlot> save(DeliveryTimeSlot deliveryTimeSlot) {
        return jpaRepository.save(toEntity(deliveryTimeSlot))
                .map(this::toDomain);
    }

    private DeliveryTimeSlotEntity toEntity(DeliveryTimeSlot slot) {
        DeliveryTimeSlotEntity entity = new DeliveryTimeSlotEntity();
        entity.setId(slot.getId());
        entity.setStartTime(slot.getStartTime());
        entity.setEndTime(slot.getEndTime());
        entity.setDeliveryMethodId(slot.getDeliveryMethodId());
        return entity;

    }

    private DeliveryTimeSlot toDomain(DeliveryTimeSlotEntity entity) {
        DeliveryTimeSlot slot = new DeliveryTimeSlot();
        slot.setId(entity.getId());
        slot.setStartTime(entity.getStartTime());
        slot.setEndTime(entity.getEndTime());
        slot.setDeliveryMethodId(entity.getDeliveryMethodId());
        return slot;
    }
}
