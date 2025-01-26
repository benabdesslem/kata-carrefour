package com.test.driveanddeliver.infrastructure.config;

import com.test.driveanddeliver.application.dto.BookingRequest;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.SenderOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    private final String groupId = "group_0";
    private final String bookingsReceiverTopic = "bookings_receiver_topic";

    @Bean
    public ReceiverOptions<String, BookingRequest> receiverOptions() {
        return ReceiverOptions.<String, BookingRequest>create()
                .subscription(Collections.singleton(bookingsReceiverTopic))
                .consumerProperty("bootstrap.servers", bootstrapServers)
                .consumerProperty("group.id", groupId)
                .withKeyDeserializer(new StringDeserializer())
                .withValueDeserializer(new JsonDeserializer<>(BookingRequest.class));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, BookingRequest> kafkaConsumerTemplate(ReceiverOptions<String, BookingRequest> receiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
    }

    @Bean
    public Flux<ReceiverRecord<String, BookingRequest>> kafkaReceiverFlux(ReactiveKafkaConsumerTemplate<String, BookingRequest> kafkaConsumerTemplate) {
        return kafkaConsumerTemplate.receive();
    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put("bootstrap.servers", bootstrapServers);
        producerProps.put("key.serializer", StringSerializer.class);
        producerProps.put("value.serializer", StringSerializer.class);
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(producerProps));
    }
}