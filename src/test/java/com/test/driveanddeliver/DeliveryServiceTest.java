package com.test.driveanddeliver;


import com.test.driveanddeliver.domain.exception.AlreadyBookedException;
import com.test.driveanddeliver.domain.exception.NotFoundException;
import com.test.driveanddeliver.domain.model.CreateDeliveryTimeSlotCommand;
import com.test.driveanddeliver.domain.model.DeliveryMethod;
import com.test.driveanddeliver.domain.model.DeliveryTimeSlot;
import com.test.driveanddeliver.domain.model.User;
import com.test.driveanddeliver.domain.repository.BookingRepository;
import com.test.driveanddeliver.domain.repository.DeliveryMethodRepository;
import com.test.driveanddeliver.domain.repository.DeliveryTimeSlotRepository;
import com.test.driveanddeliver.domain.repository.UserRepository;
import com.test.driveanddeliver.domain.service.DeliveryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @InjectMocks
    private DeliveryService deliveryService;

    @Mock
    private DeliveryMethodRepository deliveryMethodRepository;

    @Mock
    private DeliveryTimeSlotRepository deliveryTimeSlotRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void testGetAllDeliveryMethods() {
        // given
        when(deliveryMethodRepository.findAll()).thenReturn(Flux.just(new DeliveryMethod("DELIVERY_TODAY")));

        // when
        Flux<DeliveryMethod> deliveryMethods = deliveryService.getAllDeliveryMethods();

        // then
        StepVerifier.create(deliveryMethods)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testGetDeliveryTimeSlotsByMethodId_WhenValidMethodId() {
        // given
        when(deliveryMethodRepository.findById(any(Long.class))).thenReturn(Mono.just(new DeliveryMethod()));
        when(deliveryTimeSlotRepository.findByDeliveryMethodId(any())).thenReturn(Flux.just(new DeliveryTimeSlot()));

        // when
        Flux<DeliveryTimeSlot> deliveryTimeSlots = deliveryService.getDeliveryTimeSlotsByMethodId(1L);

        // then
        StepVerifier.create(deliveryTimeSlots)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testGetDeliveryTimeSlotsByMethodId_WhenInvalidMethodId() {
        // given
        when(deliveryMethodRepository.findById(any(Long.class))).thenReturn(Mono.empty());

        // when
        Mono<DeliveryTimeSlot> deliveryTimeSlots = deliveryService.getDeliveryTimeSlotsByMethodId(999L).next();

        // then
        StepVerifier.create(deliveryTimeSlots)
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void testBookDeliveryTimeSlot_WhenTimeSlotNotFound_ShouldThrowNotFoundException() {
        // given
        when(deliveryTimeSlotRepository.findById(any(Long.class))).thenReturn(Mono.empty());

        // when
        Mono<Void> result = deliveryService.bookDeliveryTimeSlot(1L, "user");

        // then
        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();
    }


    @Test
    void testBookDeliveryTimeSlot_WhenAlreadyBooked_ShouldThrowAlreadyBookedException() {
        // given
        var deliveryTimeSlot = new DeliveryTimeSlot();
        deliveryTimeSlot.setId(1L);

        when(deliveryTimeSlotRepository.findById(any(Long.class))).thenReturn(Mono.just(deliveryTimeSlot));
        when(bookingRepository.existsByUserIdAndDeliveryTimeSlotId(any(Long.class), any(Long.class))).thenReturn(Mono.just(true));
        var user = new User();
        user.setId(1L);
        when(userRepository.findByLogin(any())).thenReturn(Mono.just(user));

        // when
        Mono<Void> result = deliveryService.bookDeliveryTimeSlot(1L, "user");

        // then
        StepVerifier.create(result)
                .expectError(AlreadyBookedException.class)
                .verify();
    }

    @Test
    void testBookDeliveryTimeSlot_WhenAllConditionsMet_ShouldBookSuccessfully() {
        // given
        var deliveryTimeSlot = new DeliveryTimeSlot();
        deliveryTimeSlot.setId(1L);
        when(deliveryTimeSlotRepository.findById(any(Long.class))).thenReturn(Mono.just(deliveryTimeSlot));
        when(bookingRepository.existsByUserIdAndDeliveryTimeSlotId(any(), any())).thenReturn(Mono.just(false));
        when(bookingRepository.save(any())).thenReturn(Mono.empty());
        when(userRepository.findByLogin(any())).thenReturn(Mono.just(new User()));

        // when
        Mono<Void> result = deliveryService.bookDeliveryTimeSlot(1L, "user");

        // then
        StepVerifier.create(result)
                .verifyComplete();
    }


    @Test
    void testAddDeliveryTimeSlot_NotFound() {
        // given
        when(deliveryMethodRepository.findById(any(Long.class))).thenReturn(Mono.empty());

        // when
        Mono<Void> result = deliveryService.addDeliveryTimeSlot(1L, new CreateDeliveryTimeSlotCommand()).then();

        // then
        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void testAddDeliveryTimeSlot_Success() {
        // given
        when(deliveryMethodRepository.findById(any(Long.class))).thenReturn(Mono.just(new DeliveryMethod()));
        when(deliveryTimeSlotRepository.save(any())).thenReturn(Mono.just(new DeliveryTimeSlot()));

        // when
        var command = new CreateDeliveryTimeSlotCommand();
        command.setStartTime(ZonedDateTime.now());
        command.setEndTime(ZonedDateTime.now().plusHours(1));
        Mono<Void> result = deliveryService.addDeliveryTimeSlot(1L, command).then();

        // then
        StepVerifier.create(result)
                .verifyComplete();
    }

}
