package com.test.driveanddeliver.application.service;

import com.test.driveanddeliver.application.dto.BookingRequest;
import com.test.driveanddeliver.application.dto.BookingResponse;
import com.test.driveanddeliver.application.port.DeliveryServicePort;
import com.test.driveanddeliver.domain.exception.AlreadyBookedException;
import com.test.driveanddeliver.domain.exception.NotFoundException;
import com.test.driveanddeliver.domain.model.CreateDeliveryTimeSlotCommand;
import com.test.driveanddeliver.domain.model.DeliveryMethod;
import com.test.driveanddeliver.domain.model.DeliveryTimeSlot;
import com.test.driveanddeliver.domain.service.DeliveryService;
import com.test.driveanddeliver.application.dto.DeliveryTimeSlotRequest;
import com.test.driveanddeliver.infrastructure.exception.UnauthorizedAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DeliveryApplicationService implements DeliveryServicePort {
    private static final Logger log = LoggerFactory.getLogger(DeliveryApplicationService.class);

    private final DeliveryService deliveryService;

    public DeliveryApplicationService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }


    @Cacheable("deliveryMethods")
    @Override
    public Flux<DeliveryMethod> getAllDeliveryMethods() {
        return deliveryService.getAllDeliveryMethods();
    }


    @Cacheable(value = "deliveryTimeSlots", key = "#deliveryMethodId")
    @Override
    public Flux<DeliveryTimeSlot> getDeliveryTimeSlotsByMethodId(Long deliveryMethodId) {
        log.info("Getting delivery time slots for delivery method id: {}", deliveryMethodId);
        return deliveryService.getDeliveryTimeSlotsByMethodId(deliveryMethodId);
    }

    @Override
    public Mono<Void> bookDeliveryTimeSlot(Long deliveryTimeSlotId) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .switchIfEmpty(Mono.error(new UnauthorizedAccessException("user.notAuthenticated", "User not authenticated to book time slot")))
                .map(Authentication::getPrincipal)
                .cast(User.class)
                .map(User::getUsername)
                .flatMap(username -> deliveryService.bookDeliveryTimeSlot(deliveryTimeSlotId, username));
    }

    @CacheEvict(value = "deliveryTimeSlots", key = "#deliveryMethodId")
    @Override
    public Mono<DeliveryTimeSlot> addDeliveryTimeSlot(Long deliveryMethodId, DeliveryTimeSlotRequest request) {
        CreateDeliveryTimeSlotCommand command = new CreateDeliveryTimeSlotCommand();
        command.setStartTime(request.getStartTime());
        command.setEndTime(request.getEndTime());

        return deliveryService.addDeliveryTimeSlot(deliveryMethodId, command);
    }

    @Override
    public Mono<BookingResponse> eventBooking(BookingRequest bookingRequest) {
        return deliveryService.eventBooking(bookingRequest.getTimeSlotId(), bookingRequest.getUserId())
                .map(booking -> new BookingResponse(true, null))
                .onErrorResume(error -> {
                    if (error instanceof NotFoundException || error instanceof AlreadyBookedException) {
                        return Mono.just(new BookingResponse(false, error.getMessage()));
                    }
                    return Mono.error(error);
                });

    }
}