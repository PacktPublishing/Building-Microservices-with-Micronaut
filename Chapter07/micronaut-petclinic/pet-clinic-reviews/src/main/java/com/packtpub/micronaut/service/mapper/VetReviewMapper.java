package com.packtpub.micronaut.service.mapper;


import com.packtpub.micronaut.domain.VetReview;
import com.packtpub.micronaut.service.dto.VetReviewDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link VetReview} and its DTO {@link VetReviewDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {})
public interface VetReviewMapper extends EntityMapper<VetReviewDTO, VetReview> {

    default VetReview fromReviewId(String reviewId) {
        if (reviewId == null) {
            return null;
        }
        VetReview vetReview = new VetReview();
        vetReview.setReviewId(reviewId);
        return vetReview;
    }
}
