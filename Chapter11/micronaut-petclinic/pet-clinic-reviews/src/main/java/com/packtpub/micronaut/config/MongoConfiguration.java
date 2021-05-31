package com.packtpub.micronaut.config;
import io.micronaut.context.annotation.Value;

public class MongoConfiguration {

    @Value("${mongodb.databaseName}")
    private String databaseName;

    @Value("${mongodb.collectionName}")
    private String collectionName;

    public String getDatabaseName() {
        return databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }
}
