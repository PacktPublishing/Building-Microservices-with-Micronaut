package com.packtpub.micronaut.web.rest.client.petowner;

import com.packtpub.micronaut.service.dto.petowner.PetTypeDTO;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

import java.util.List;
import java.util.Optional;

@Client(id = "pet-owner")
public interface PetTypeResourceClient {

    @Post("/api/pet-types")
    HttpResponse<PetTypeDTO> createPetType(@Body PetTypeDTO petTypeDTO);

    @Put("/api/pet-types")
    HttpResponse<PetTypeDTO> updatePetType(@Body PetTypeDTO petTypeDTO);

    @Get("/api/pet-types")
    HttpResponse<List<PetTypeDTO>> getAllPetTypes(HttpRequest request, Pageable pageable);

    @Get("/api/pet-types/{id}")
    Optional<PetTypeDTO> getPetType(@PathVariable Long id);

    @Delete("/api/pet-types/{id}")
    HttpResponse deletePetType(@PathVariable Long id);
}
