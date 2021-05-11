package com.packtpub.micronaut.web.rest.client.petclinicreviews;

import com.packtpub.micronaut.service.dto.petclinicreviews.VetReviewDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

import java.util.List;
import java.util.Optional;

@Client(id = "pet-clinic-reviews")
public interface VetReviewResourceClient {

    @Post("/api/vet-reviews")
    HttpResponse<VetReviewDTO> createVetReview(@Body VetReviewDTO vetReviewDTO);

    @Put("/api/vet-reviews")
    HttpResponse<VetReviewDTO> updateVetReview(@Body VetReviewDTO vetReviewDTO);

    @Get("/api/vet-reviews")
    HttpResponse<List<VetReviewDTO>> getAllVetReviews(HttpRequest request);

    @Get("/api/vet-reviews/{reviewId}")
    Optional<VetReviewDTO> getVetReview(@PathVariable String reviewId);

    @Delete("/api/vet-reviews/{reviewId}")
    HttpResponse deleteVetReview(@PathVariable String reviewId);
}
