package com.packtpub.micronaut.web.rest.client.petowner;

import com.packtpub.micronaut.service.dto.petowner.PetDTO;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;

import java.util.List;
import java.util.Optional;

@Client(id = "pet-owner")
@CircuitBreaker(delay = "5s", attempts = "5", multiplier = "3", reset = "300s")
public interface PetResourceClient {

    @Post("/api/pets")
    HttpResponse<PetDTO> createPet(@Body PetDTO petDTO);

    @Put("/api/pets")
    HttpResponse<PetDTO> updatePet(@Body PetDTO petDTO);

    @Get("/api/pets")
    HttpResponse<List<PetDTO>> getAllPets(HttpRequest request, Pageable pageable);

    @Get("/api/pets/{id}")
    Optional<PetDTO> getPet(@PathVariable Long id);

    @Delete("/api/pets/{id}")
    HttpResponse deletePet(@PathVariable Long id);
}
