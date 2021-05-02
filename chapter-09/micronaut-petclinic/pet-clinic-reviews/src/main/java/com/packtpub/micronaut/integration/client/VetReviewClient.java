package com.packtpub.micronaut.integration.client;

import com.packtpub.micronaut.service.dto.VetReviewDTO;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;

@KafkaClient
public interface VetReviewClient {

    @Topic("vet-reviews")
    void send(@Body VetReviewDTO vetReview);
}
