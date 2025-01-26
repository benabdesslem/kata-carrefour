package com.test.driveanddeliver.infrastructure.resource;

import com.test.driveanddeliver.application.port.DeliveryServicePort;
import com.test.driveanddeliver.application.service.DeliveryApplicationService;
import com.test.driveanddeliver.domain.model.DeliveryMethod;
import com.test.driveanddeliver.domain.model.DeliveryTimeSlot;
import com.test.driveanddeliver.application.dto.DeliveryTimeSlotRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryResource {
    private final DeliveryServicePort deliveryServicePort;

    public DeliveryResource(DeliveryServicePort deliveryServicePort) {
        this.deliveryServicePort = deliveryServicePort;
    }

    @Operation(summary = "Get all delivery methods")
    @GetMapping("/methods")
    public Flux<DeliveryMethod> getAllDeliveryMethods() {
        return deliveryServicePort.getAllDeliveryMethods();
    }

    @Operation(summary = "Get delivery time slots by method ID",
            description = "Returns the delivery time slots for the specified delivery method ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of delivery time slots"),
            @ApiResponse(responseCode = "404", description = "Delivery method not found")
    })
    @GetMapping("/timeslots/{deliveryMethodId}")
    public Flux<DeliveryTimeSlot> getDeliveryTimeSlotsByMethodId(@PathVariable Long deliveryMethodId) {
        return deliveryServicePort.getDeliveryTimeSlotsByMethodId(deliveryMethodId);
    }


    @Operation(summary = "Book a delivery time slot",
            description = "Books a delivery time slot for the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery time slot booked successfully"),
            @ApiResponse(responseCode = "404", description = "Delivery time slot not found or user already booked this time slot"),
            @ApiResponse(responseCode = "409", description = "Delivery time slot already booked")
    })

    @PostMapping("/book")
    public Mono<ResponseEntity<Void>> bookDeliveryTimeSlot(@RequestParam Long deliveryTimeSlotId) {
        return deliveryServicePort.bookDeliveryTimeSlot(deliveryTimeSlotId)
                .then(Mono.just(ResponseEntity.ok().build()));
    }


    @Operation(summary = "Add delivery time slot for a delivery method ID",
            description = "Adds a new delivery time slot for the specified delivery method ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Delivery time slot added successfully"),
            @ApiResponse(responseCode = "404", description = "Delivery method not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/timeslots/{deliveryMethodId}")
    public Mono<ResponseEntity<Void>> addDeliveryTimeSlot(@PathVariable Long deliveryMethodId,
                                                          @Valid @RequestBody DeliveryTimeSlotRequest request) {
        return deliveryServicePort.addDeliveryTimeSlot(deliveryMethodId, request)
                .then(Mono.just(ResponseEntity.created(null).build()));
    }

}