package com.packtpub.micronaut.web.rest.client.petclinic;

import com.packtpub.micronaut.service.dto.petclinic.VetDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

import java.util.List;
import java.util.Optional;

@Client(id = "pet-clinic")
public interface VetResourceClient {

    @Post("/api/vets")
    HttpResponse<VetDTO> createVet(@Body VetDTO vetDTO);

    @Put("/api/vets")
    HttpResponse<VetDTO> updateVet(@Body VetDTO vetDTO);

    @Get("/api/vets")
    HttpResponse<List<VetDTO>> getAllVets(HttpRequest request);

    @Get("/api/vets/{id}")
    Optional<VetDTO> getVet(@PathVariable Long id);

    @Delete("/api/vets/{id}")
    HttpResponse deleteVet(@PathVariable Long id);
}
