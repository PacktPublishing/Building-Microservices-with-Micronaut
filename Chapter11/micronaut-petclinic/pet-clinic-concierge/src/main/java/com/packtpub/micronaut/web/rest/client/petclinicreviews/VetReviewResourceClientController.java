package com.packtpub.micronaut.web.rest.client.petclinicreviews;

import com.packtpub.micronaut.service.dto.petclinicreviews.VetReviewDTO;
import com.packtpub.micronaut.web.rest.client.petclinic.VetResourceClient;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller("/api")
@Secured(SecurityRule.IS_ANONYMOUS)
public class VetReviewResourceClientController {

    @Inject
    VetReviewResourceClient vetReviewResourceClient;

    @NewSpan
    @Post("/vet-reviews")

    public HttpResponse<VetReviewDTO> createVetReview(VetReviewDTO vetReviewDTO) {
        return vetReviewResourceClient.createVetReview(vetReviewDTO);
    }

    @NewSpan
    @Put("/vet-reviews")
    public HttpResponse<VetReviewDTO> updateVetReview(VetReviewDTO vetReviewDTO) {
        return vetReviewResourceClient.updateVetReview(vetReviewDTO);
    }

    @NewSpan
    @Get("/vet-reviews")
    public HttpResponse<List<VetReviewDTO>> getAllVetReviews(HttpRequest request) {
        return vetReviewResourceClient.getAllVetReviews(request);
    }

    @NewSpan
    @Get("/vet-reviews/{reviewId}")
    public Optional<VetReviewDTO> getVetReview(String reviewId) {
        return vetReviewResourceClient.getVetReview(reviewId);
    }

    @NewSpan
    @Delete("/vet-reviews/{reviewId}")
    public HttpResponse deleteVetReview(String reviewId) {
        return vetReviewResourceClient.deleteVetReview(reviewId);
    }
}
