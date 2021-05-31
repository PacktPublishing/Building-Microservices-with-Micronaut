package com.packtpub.micronaut.web.rest.client.petowner;

import com.packtpub.micronaut.service.dto.petowner.OwnerDTO;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;

import java.util.List;
import java.util.Optional;

@Retryable(attempts = "5", delay = "2s", multiplier = "1.5", maxDelay = "20s")
@Client(id = "pet-owner")
public interface OwnerResourceClient {

    @Post("/api/owners")
    HttpResponse<OwnerDTO> createOwner(@Body OwnerDTO ownerDTO);

    @Put("/api/owners")
    HttpResponse<OwnerDTO> updateOwner(@Body OwnerDTO ownerDTO);

    @Get("/api/owners")
    HttpResponse<List<OwnerDTO>> getAllOwners(HttpRequest request, Pageable pageable);

    @Get("/api/owners/{id}")
    Optional<OwnerDTO> getOwner(@PathVariable Long id);

    @Delete("/api/owners/{id}")
    HttpResponse deleteOwner(@PathVariable Long id);
}
