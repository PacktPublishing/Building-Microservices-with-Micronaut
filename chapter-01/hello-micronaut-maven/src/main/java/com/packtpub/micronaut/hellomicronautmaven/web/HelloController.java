package com.packtpub.micronaut.hellomicronautmaven.web;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/")
public class HelloController {

    @Get("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloMicronaut() {
        return "Hello, PackT Micronaut!";
    }
}