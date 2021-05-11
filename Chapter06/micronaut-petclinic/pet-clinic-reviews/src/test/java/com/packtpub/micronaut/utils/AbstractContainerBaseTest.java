package com.packtpub.micronaut.utils;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class AbstractContainerBaseTest {

    public static final MongoDBContainer MONGO_DB_CONTAINER;

    static {
        MONGO_DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
        MONGO_DB_CONTAINER.start();
    }
}
