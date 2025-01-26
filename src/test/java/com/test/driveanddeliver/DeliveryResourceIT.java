package com.test.driveanddeliver;

import com.test.driveanddeliver.application.dto.DeliveryTimeSlotRequest;
import com.test.driveanddeliver.application.service.DeliveryApplicationService;
import com.test.driveanddeliver.domain.model.DeliveryMethod;
import com.test.driveanddeliver.domain.model.DeliveryTimeSlot;
import com.test.driveanddeliver.infrastructure.repository.JpaDeliveryMethodRepository;
import com.test.driveanddeliver.infrastructure.resource.DeliveryResource;
import com.test.driveanddeliver.infrastructure.resource.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@SpringBootTest
@WithMockUser
class DeliveryResourceIT {


    private WebTestClient webTestClient;

    @Autowired
    private DeliveryApplicationService customerService;

    @Autowired
    private JpaDeliveryMethodRepository deliveryMethodRepository;


    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToController(new DeliveryResource(customerService))
                .controllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    void testGetAllDeliveryMethods() {
        webTestClient.get().uri("/api/delivery/methods")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DeliveryMethod.class);
    }


    @Test
    void testGetDeliveryTimeSlotsByMethodId() {
        var driveDeliveryMethod = deliveryMethodRepository.findByName("DRIVE").block();
        webTestClient.get().uri("/api/delivery/timeslots/{deliveryMethodId}", driveDeliveryMethod.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DeliveryTimeSlot.class);
    }


    @Test
    void testGetDeliveryTimeSlotsByMethodId_notFound() {
        webTestClient.get().uri("/api/delivery/timeslots/{deliveryMethodId}", 100L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.errorKey").isEqualTo("Not Found")
                .jsonPath("$.message").isEqualTo("Delivery method not found for id: 100");
    }

    @Test
    void testBookDeliveryTimeSlot() {
        webTestClient.post().uri("/api/delivery/book?deliveryTimeSlotId={deliveryTimeSlotId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testAddDeliveryTimeSlot_Success() {
        // Given
        Long deliveryMethodId = 1L;
        DeliveryTimeSlotRequest request = new DeliveryTimeSlotRequest();
        request.setStartTime(ZonedDateTime.now());
        request.setEndTime(ZonedDateTime.now().plusHours(1));

        // When
        webTestClient.post()
                .uri("/api/delivery/timeslots/{deliveryMethodId}", deliveryMethodId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), DeliveryTimeSlotRequest.class)
                .exchange()
                // Then
                .expectStatus().isCreated()
                .expectBody(Void.class);
    }

    @Test
    void testAddDeliveryTimeSlot_NotFound() {
        // Given
        Long deliveryMethodId = 100L;
        DeliveryTimeSlotRequest request = new DeliveryTimeSlotRequest();
        request.setStartTime(ZonedDateTime.now());
        request.setEndTime(ZonedDateTime.now().plusHours(1));

        // When
        webTestClient.post()
                .uri("/api/delivery/timeslots/{deliveryMethodId}", deliveryMethodId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), DeliveryTimeSlotRequest.class)
                .exchange()
                // Then
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.errorKey").isEqualTo("Not Found")
                .jsonPath("$.message").isEqualTo("Delivery method not found for id: 100");
    }
}