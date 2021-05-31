package com.packtpub.micronaut.integration.client;

import com.packtpub.micronaut.integration.dto.VetReviewDTO;
import com.packtpub.micronaut.service.VetService;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KafkaListener(groupId = "pet-clinic")
public class VetReviewListener {

    private static final Logger log = LoggerFactory.getLogger(VetReviewListener.class);

    private final VetService vetService;

    public VetReviewListener(VetService vetService) {
        this.vetService = vetService;
    }

    @Topic("vet-reviews")
    public void receive(@Body VetReviewDTO vetReview) {
        log.info("Received: vetReview -> {}", vetReview);
        try {
            vetService.updateVetAverageRating(vetReview.getVetId(), vetReview.getRating());
        } catch (Exception e) {
            log.error("Exception occurred: {}", e.toString());
        }
    }
}
