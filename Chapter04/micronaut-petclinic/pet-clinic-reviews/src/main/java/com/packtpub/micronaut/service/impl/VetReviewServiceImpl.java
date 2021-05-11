package com.packtpub.micronaut.service.impl;

import com.packtpub.micronaut.domain.VetReview;
import com.packtpub.micronaut.repository.VetReviewRepository;
import com.packtpub.micronaut.service.VetReviewService;
import com.packtpub.micronaut.service.dto.VetReviewDTO;
import com.packtpub.micronaut.service.mapper.VetReviewMapper;

import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class VetReviewServiceImpl implements VetReviewService {

    private final VetReviewRepository vetReviewRepository;

    private final VetReviewMapper vetReviewMapper;

    public VetReviewServiceImpl(VetReviewRepository vetReviewRepository, VetReviewMapper vetReviewMapper) {
        this.vetReviewRepository = vetReviewRepository;
        this.vetReviewMapper = vetReviewMapper;
    }

    @Override
    public List<VetReviewDTO> findAll() {
        return vetReviewRepository.findAll()
                .stream()
                .map(vetReviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public VetReviewDTO findByReviewId(String reviewId) {
        return vetReviewMapper.toDto(vetReviewRepository.findByReviewId(reviewId));
    }

    @Override
    public VetReviewDTO save(VetReviewDTO vetReviewDTO) {
        VetReview vetReview = vetReviewMapper.toEntity(vetReviewDTO);
        return vetReviewMapper.toDto(vetReviewRepository.save(vetReview));
    }

    @Override
    public void delete(String reviewId) {
        vetReviewRepository.delete(reviewId);
    }
}
