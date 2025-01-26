package com.test.driveanddeliver.infrastructure.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.driveanddeliver.application.dto.BookingRequest;
import com.test.driveanddeliver.application.dto.BookingResponse;
import com.test.driveanddeliver.application.port.DeliveryServicePort;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;


@Component
public class BookingMessageListener {

    private static final Logger log = LoggerFactory.getLogger(BookingMessageListener.class);

    private final String bookingsResponseTopic = "bookings_response_topic";

    private final ReactiveKafkaConsumerTemplate<String, BookingRequest> kafkaConsumerTemplate;

    private final ReactiveKafkaProducerTemplate<String, String> kafkaProducerTemplate;

    private final ObjectMapper objectMapper;

    private final DeliveryServicePort deliveryServicePort;

    public BookingMessageListener(ReactiveKafkaConsumerTemplate<String, BookingRequest> kafkaConsumerTemplate,
                                  ReactiveKafkaProducerTemplate<String, String> kafkaProducerTemplate,
                                  ObjectMapper objectMapper,
                                  DeliveryServicePort deliveryServicePort) {
        this.kafkaConsumerTemplate = kafkaConsumerTemplate;
        this.kafkaProducerTemplate = kafkaProducerTemplate;
        this.objectMapper = objectMapper;
        this.deliveryServicePort = deliveryServicePort;
    }

    @PostConstruct
    public void listenAndHandleMessages() {
        Flux<ReceiverRecord<String, BookingRequest>> kafkaFlux = kafkaConsumerTemplate.receive();

        kafkaFlux.subscribe(record -> {
            BookingRequest bookingRequest = record.value();
            log.info("Received booking request: {}", bookingRequest);

            BookingResponse bookingResponse = deliveryServicePort.eventBooking(bookingRequest).block();
            sendMessageToTopic(bookingResponse);
        });
    }

    private void sendMessageToTopic(BookingResponse bookingResponse) {
        try {
            String bookingRequestJson = objectMapper.writeValueAsString(bookingResponse);
            kafkaProducerTemplate.send(bookingsResponseTopic, null, bookingRequestJson).subscribe();
        } catch (JsonProcessingException e) {
            log.error("Error while sending message to Kafka topic", e);
        }
    }
}