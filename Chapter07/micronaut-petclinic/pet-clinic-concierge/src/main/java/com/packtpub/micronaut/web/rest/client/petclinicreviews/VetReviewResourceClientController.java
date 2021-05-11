package com.packtpub.micronaut.web.rest.client.petclinicreviews;

import com.packtpub.micronaut.service.dto.petclinicreviews.VetReviewDTO;
import com.packtpub.micronaut.web.rest.client.petclinic.VetResourceClient;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller("/api")
public class VetReviewResourceClientController {

    @Inject
    VetReviewResourceClient vetReviewResourceClient;

    @Post("/vet-reviews")
    public HttpResponse<VetReviewDTO> createVetReview(VetReviewDTO vetReviewDTO) {
        return vetReviewResourceClient.createVetReview(vetReviewDTO);
    }

    @Put("/vet-reviews")
    public HttpResponse<VetReviewDTO> updateVetReview(VetReviewDTO vetReviewDTO) {
        return vetReviewResourceClient.updateVetReview(vetReviewDTO);
    }

    @Get("/vet-reviews")
    public HttpResponse<List<VetReviewDTO>> getAllVetReviews(HttpRequest request) {
        return vetReviewResourceClient.getAllVetReviews(request);
    }

    @Get("/vet-reviews/{reviewId}")
    public Optional<VetReviewDTO> getVetReview(String reviewId) {
        return vetReviewResourceClient.getVetReview(reviewId);
    }

    @Delete("/vet-reviews/{reviewId}")
    public HttpResponse deleteVetReview(String reviewId) {
        return vetReviewResourceClient.deleteVetReview(reviewId);
    }
}
