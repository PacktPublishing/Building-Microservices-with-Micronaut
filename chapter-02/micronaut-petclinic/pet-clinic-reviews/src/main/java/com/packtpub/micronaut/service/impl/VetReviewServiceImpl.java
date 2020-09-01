package com.packtpub.micronaut.service.impl;

import com.packtpub.micronaut.domain.VetReview;
import com.packtpub.micronaut.repository.VetReviewRepository;
import com.packtpub.micronaut.service.VetReviewService;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class VetReviewServiceImpl implements VetReviewService {

    private final VetReviewRepository vetReviewRepository;

    public VetReviewServiceImpl(VetReviewRepository vetReviewRepository) {
        this.vetReviewRepository = vetReviewRepository;
    }

    @Override
    public List<VetReview> findAll() {
        return vetReviewRepository.findAll();
    }

    @Override
    public VetReview findByReviewId(String reviewId) {
        return vetReviewRepository.findByReviewId(reviewId);
    }

    @Override
    public VetReview save(VetReview vetReview) {
        return vetReviewRepository.save(vetReview);
    }

    @Override
    public void delete(String reviewId) {
        vetReviewRepository.delete(reviewId);
    }
}
