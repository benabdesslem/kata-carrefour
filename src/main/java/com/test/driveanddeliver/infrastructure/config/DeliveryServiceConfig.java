package com.test.driveanddeliver.infrastructure.config;

import com.test.driveanddeliver.domain.repository.BookingRepository;
import com.test.driveanddeliver.domain.repository.DeliveryMethodRepository;
import com.test.driveanddeliver.domain.repository.DeliveryTimeSlotRepository;
import com.test.driveanddeliver.domain.repository.UserRepository;
import com.test.driveanddeliver.domain.service.DeliveryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeliveryServiceConfig {

    private final DeliveryMethodRepository deliveryMethodRepository;
    private final DeliveryTimeSlotRepository deliveryTimeSlotRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public DeliveryServiceConfig(DeliveryMethodRepository deliveryMethodRepository,
                                 DeliveryTimeSlotRepository deliveryTimeSlotRepository,
                                 BookingRepository bookingRepository,
                                 UserRepository userRepository) {
        this.deliveryMethodRepository = deliveryMethodRepository;
        this.deliveryTimeSlotRepository = deliveryTimeSlotRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public DeliveryService deliveryService() {
        return new DeliveryService(deliveryMethodRepository, deliveryTimeSlotRepository, bookingRepository, userRepository);
    }
}