package com.packtpub.micronaut.repository.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.packtpub.micronaut.config.MongoConfiguration;
import com.packtpub.micronaut.domain.VetReview;
import com.packtpub.micronaut.repository.VetReviewRepository;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Singleton
public class VetReviewRepositoryImpl implements VetReviewRepository {

    private final MongoClient mongoClient;

    private final MongoConfiguration mongoConfiguration;

    public VetReviewRepositoryImpl(MongoClient mongoClient,
                                   MongoConfiguration mongoConfiguration) {
        this.mongoClient = mongoClient;
        this.mongoConfiguration = mongoConfiguration;
    }

    @Override
    public List<VetReview> findAll() {
        List<VetReview> vetReviews = new ArrayList<>();
        getCollection().find().forEach(vetReview -> {
            vetReviews.add(vetReview);
        });
        return vetReviews;
    }

    @Override
    public VetReview findByReviewId(String reviewId) {
        return getCollection().find(eq("reviewId", reviewId)).first();
    }

    @Override
    public VetReview save(VetReview vetReview) {
        getCollection().insertOne(vetReview).getInsertedId();
        return findByReviewId(vetReview.getReviewId());
    }

    @Override
    public void delete(String reviewId) {
        getCollection().deleteOne(eq("reviewId", reviewId));
    }

    private MongoCollection<VetReview> getCollection() {
        return mongoClient
                .getDatabase(mongoConfiguration.getDatabaseName())
                .getCollection(mongoConfiguration.getCollectionName(), VetReview.class);
    }
}
