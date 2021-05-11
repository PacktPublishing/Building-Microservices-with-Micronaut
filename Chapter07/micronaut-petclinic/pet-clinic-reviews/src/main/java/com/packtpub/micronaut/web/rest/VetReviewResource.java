package com.packtpub.micronaut.web.rest;

import com.packtpub.micronaut.integration.client.VetReviewClient;
import com.packtpub.micronaut.service.VetReviewService;
import com.packtpub.micronaut.service.dto.VetReviewDTO;
import com.packtpub.micronaut.util.HeaderUtil;
import com.packtpub.micronaut.web.rest.errors.BadRequestAlertException;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.packtpub.micronaut.domain.VetReview}.
 */
@Controller("/api")
public class VetReviewResource {

    private final Logger log = LoggerFactory.getLogger(VetReviewResource.class);

    private static final String ENTITY_NAME = "vetReview";

    @Value("${micronaut.application.name}")
    private String applicationName;

    private final VetReviewService vetReviewService;

    private final VetReviewClient vetReviewClient;

    public VetReviewResource(VetReviewService vetReviewService,
                             VetReviewClient vetReviewClient) {
        this.vetReviewService = vetReviewService;
        this.vetReviewClient = vetReviewClient;
    }

    /**
     * {@code POST  /vet-reviews} : Create a new vetReview.
     *
     * @param vetReviewDTO the vetReviewDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new vetReviewDTO, or with status {@code 400 (Bad Request)} if the vetReview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/vet-reviews")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<VetReviewDTO> createVetReview(@Body VetReviewDTO vetReviewDTO) throws URISyntaxException {
        log.debug("REST request to save VetReview : {}", vetReviewDTO);
        if (vetReviewDTO.getReviewId() != null) {
            throw new BadRequestAlertException("A new vetReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VetReviewDTO result = vetReviewService.save(vetReviewDTO);

        /** Stream to other services */
        vetReviewClient.send(result);

        URI location = new URI("/api/vet-reviews/" + result.getReviewId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getReviewId());
        });
    }

    /**
     * {@code PUT  /vet-reviews} : Updates an existing vetReview.
     *
     * @param vetReviewDTO the vetReviewDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated vetReviewDTO,
     * or with status {@code 400 (Bad Request)} if the vetReviewDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vetReviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/vet-reviews")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<VetReviewDTO> updateVetReview(@Body VetReviewDTO vetReviewDTO) throws URISyntaxException {
        log.debug("REST request to update VetReview : {}", vetReviewDTO);
        if (vetReviewDTO.getReviewId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VetReviewDTO result = vetReviewService.save(vetReviewDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, vetReviewDTO.getReviewId().toString()));
    }

    /**
     * {@code GET  /vet-reviews} : get all the vetReviews.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of vetReviews in body.
     */
    @Get("/vet-reviews")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<VetReviewDTO>> getAllVetReviews(HttpRequest request) {
        log.debug("REST request to get a page of VetReviews");
        List<VetReviewDTO> list = vetReviewService.findAll();
        return HttpResponse.ok(list);
    }

    /**
     * {@code GET  /vet-reviews/:id} : get the "id" vetReview.
     *
     * @param reviewId the id of the vetReviewDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the vetReviewDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/vet-reviews/{reviewId}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<VetReviewDTO> getVetReview(@PathVariable String reviewId) {
        log.debug("REST request to get VetReview : {}", reviewId);
        return Optional.ofNullable(vetReviewService.findByReviewId(reviewId));
    }

    /**
     * {@code DELETE  /vet-reviews/:id} : delete the "id" vetReview.
     *
     * @param reviewId the id of the vetReviewDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/vet-reviews/{reviewId}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteVetReview(@PathVariable String reviewId) {
        log.debug("REST request to delete VetReview : {}", reviewId);
        vetReviewService.delete(reviewId);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, reviewId));
    }
}
