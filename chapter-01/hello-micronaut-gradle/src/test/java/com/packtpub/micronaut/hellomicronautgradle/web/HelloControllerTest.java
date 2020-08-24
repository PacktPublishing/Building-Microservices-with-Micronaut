package com.packtpub.micronaut.hellomicronautgradle.web;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;


@MicronautTest
class HelloControllerTest {

    @Inject
    @Client("/hello")

    RxHttpClient client;

    @Test
    void testResponseMessage() {

        HttpRequest<String> request = HttpRequest.GET("/");
        String body = client.toBlocking().retrieve(request);

        Assertions.assertNotNull(body);
        Assertions.assertEquals("Hello, Micronaut!", body);
    }
}
