package com.packtpub.micronaut.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;
import java.util.Objects;

/**
 * A vet review.
 */
public class VetReview {

    private String reviewId;
    private Long vetId;
    private Double rating;
    private String comment;
    private LocalDate dateAdded;

    public VetReview() {
    }

    @BsonCreator
    @JsonCreator
    public VetReview(
            @JsonProperty("reviewId")
            @BsonProperty("reviewId") String reviewId,
            @JsonProperty("vetId")
            @BsonProperty("vetId") Long vetId,
            @JsonProperty("rating")
            @BsonProperty("rating") Double rating,
            @JsonProperty("comment")
            @BsonProperty("comment") String comment,
            @JsonProperty("dateAdded")
            @BsonProperty("dateAdded") LocalDate dateAdded) {
        this.reviewId = reviewId;
        this.vetId = vetId;
        this.rating = rating;
        this.comment = comment;
        this.dateAdded = dateAdded;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public Long getVetId() {
        return vetId;
    }

    public void setVetId(Long vetId) {
        this.vetId = vetId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VetReview vetReview = (VetReview) o;
        return reviewId.equals(vetReview.reviewId) &&
                vetId.equals(vetReview.vetId) &&
                Objects.equals(rating, vetReview.rating) &&
                Objects.equals(comment, vetReview.comment) &&
                Objects.equals(dateAdded, vetReview.dateAdded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, vetId, rating, comment, dateAdded);
    }

    @Override
    public String toString() {
        return "VetReview{" +
                "reviewId='" + reviewId + '\'' +
                ", vetId=" + vetId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", dateAdded=" + dateAdded +
                '}';
    }
}
