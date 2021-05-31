package com.packtpub.micronaut.service;

import com.packtpub.micronaut.service.dto.VetReviewDTO;

import java.util.List;

public interface VetReviewService {

    /**
     * Get all vet reviews
     * @return list of vet reviews
     */
    List<VetReviewDTO> findAll();

    /**
     * Get a review
     * @param reviewId
     * @return
     */
    VetReviewDTO findByReviewId(String reviewId);

    /**
     * Save a vet review
     * @param vetReviewDTO
     * @return saved vet review
     */
    VetReviewDTO save(VetReviewDTO vetReviewDTO);

    /**
     * Delete a vet review
     * @param reviewId
     */
    void delete(String reviewId);
}
