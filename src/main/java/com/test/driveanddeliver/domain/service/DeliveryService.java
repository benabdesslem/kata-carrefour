package com.test.driveanddeliver.domain.service;

import com.test.driveanddeliver.domain.exception.AlreadyBookedException;
import com.test.driveanddeliver.domain.exception.NotFoundException;
import com.test.driveanddeliver.domain.model.*;
import com.test.driveanddeliver.domain.repository.BookingRepository;
import com.test.driveanddeliver.domain.repository.DeliveryMethodRepository;
import com.test.driveanddeliver.domain.repository.DeliveryTimeSlotRepository;
import com.test.driveanddeliver.domain.repository.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

public class DeliveryService {

    private final DeliveryMethodRepository deliveryMethodRepository;
    private final DeliveryTimeSlotRepository deliveryTimeSlotRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public DeliveryService(DeliveryMethodRepository deliveryMethodRepository,
                           DeliveryTimeSlotRepository deliveryTimeSlotRepository,
                           BookingRepository bookingRepository,
                           UserRepository userRepository) {
        this.deliveryMethodRepository = deliveryMethodRepository;
        this.deliveryTimeSlotRepository = deliveryTimeSlotRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }


    public Flux<DeliveryMethod> getAllDeliveryMethods() {
        return deliveryMethodRepository.findAll();
    }

    public Flux<DeliveryTimeSlot> getDeliveryTimeSlotsByMethodId(Long deliveryMethodId) {
        return deliveryMethodRepository.findById(deliveryMethodId)
                .switchIfEmpty(Mono.error(new NotFoundException("deliveryMethod.notFound", "Delivery method not found for id: " + deliveryMethodId)))
                .flatMapMany(deliveryMethod -> deliveryTimeSlotRepository.findByDeliveryMethodId(deliveryMethod.getId()));
    }

    public Mono<Void> bookDeliveryTimeSlot(Long deliveryTimeSlotId, String username) {
        return deliveryTimeSlotRepository.findById(deliveryTimeSlotId)
                .switchIfEmpty(Mono.error(new NotFoundException("deliveryTimeSlot.notFound", "Delivery time slot not found for id: " + deliveryTimeSlotId)))
                .flatMap(deliveryTimeSlot -> userRepository.findByLogin(username)
                        .flatMap(user -> bookingRepository.existsByUserIdAndDeliveryTimeSlotId(user.getId(), deliveryTimeSlot.getId())
                                .flatMap(alreadyBooked -> {
                                    if (alreadyBooked) {
                                        return Mono.error(new AlreadyBookedException("booking.alreadyBooked", "User already booked this time slot"));
                                    } else {
                                        Booking booking = new Booking(ZonedDateTime.now(), user.getId(), deliveryTimeSlot.getId());
                                        return bookingRepository.save(booking).then();
                                    }
                                })
                        ))
                .then();
    }


    public Mono<DeliveryTimeSlot> addDeliveryTimeSlot(Long deliveryMethodId, CreateDeliveryTimeSlotCommand command) {
        return deliveryMethodRepository.findById(deliveryMethodId)
                .switchIfEmpty(Mono.error(new NotFoundException("deliveryMethod.notFound", "Delivery method not found for id: " + deliveryMethodId)))
                .flatMap(deliveryMethod -> {
                    DeliveryTimeSlot deliveryTimeSlot = new DeliveryTimeSlot();
                    deliveryTimeSlot.setStartTime(command.getStartTime());
                    deliveryTimeSlot.setEndTime(command.getEndTime());
                    deliveryTimeSlot.setDeliveryMethodId(deliveryMethod.getId());
                    return deliveryTimeSlotRepository.save(deliveryTimeSlot);
                });
    }

    public Mono<Booking> eventBooking(Long timeSlotId, Long userId) {
        return Mono.zip(
                        deliveryTimeSlotRepository.findById(timeSlotId)
                                .switchIfEmpty(Mono.error(new NotFoundException("deliveryTimeSlot.notFound", "Delivery time slot not found"))),
                        userRepository.findById(userId)
                                .switchIfEmpty(Mono.error(new NotFoundException("user.notFound", "User not found")))
                )
                .flatMap(tuple -> {
                    DeliveryTimeSlot deliveryTimeSlot = tuple.getT1();
                    User user = tuple.getT2();

                    return bookingRepository.existsByUserIdAndDeliveryTimeSlotId(user.getId(), deliveryTimeSlot.getId())
                            .flatMap(alreadyBooked -> {
                                if (alreadyBooked) {
                                    return Mono.error(new AlreadyBookedException("booking.alreadyBooked", "User already booked this time slot"));
                                }
                                Booking booking = new Booking(ZonedDateTime.now(), user.getId(), deliveryTimeSlot.getId());
                                return bookingRepository.save(booking);
                            });
                });
    }


}
