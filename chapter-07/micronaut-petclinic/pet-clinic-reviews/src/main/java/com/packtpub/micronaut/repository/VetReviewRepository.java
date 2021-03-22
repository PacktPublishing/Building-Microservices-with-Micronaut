package com.packtpub.micronaut.repository;

import com.packtpub.micronaut.domain.VetReview;

import java.util.List;

public interface VetReviewRepository {

    /**
     * Get all vet reviews
     * @return the list of vet reviews
     */
    List<VetReview> findAll();

    /**
     * Get one review
     * @param reviewId
     * @return vet review
     */
    VetReview findByReviewId(String reviewId);

    /**
     * Save a vet review
     * @param vetReview
     * @return Persisted vet review
     */
    VetReview save(VetReview vetReview);

    /**
     * Delete a vet review by reviewId
     * @param reviewId
     */
    void delete(String reviewId);
}
