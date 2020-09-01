package com.packtpub.micronaut.service;

import com.packtpub.micronaut.domain.VetReview;

import java.util.List;

public interface VetReviewService {

    /**
     * Get all vet reviews
     * @return list of vet reviews
     */
    List<VetReview> findAll();

    /**
     * Get a review
     * @param reviewId
     * @return
     */
    VetReview findByReviewId(String reviewId);

    /**
     * Save a vet review
     * @param vetReview
     * @return saved vet review
     */
    VetReview save(VetReview vetReview);

    /**
     * Delete a vet review
     * @param reviewId
     */
    void delete(String reviewId);
}
