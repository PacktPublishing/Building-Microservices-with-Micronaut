package com.packtpub.micronaut.web.rest.client.petowner;

import com.packtpub.micronaut.service.dto.petowner.VisitDTO;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

import java.util.List;
import java.util.Optional;

@Client(id = "pet-owner")
public interface VisitResourceClient {

    @Post("/api/visits")
    HttpResponse<VisitDTO> createVisit(@Body VisitDTO visitDTO);

    @Put("/api/visits")
    HttpResponse<VisitDTO> updateVisit(@Body VisitDTO visitDTO);

    @Get("/api/visits")
    HttpResponse<List<VisitDTO>> getAllVisits(HttpRequest request, Pageable pageable);

    @Get("/api/visits/{id}")
    Optional<VisitDTO> getVisit(@PathVariable Long id);

    @Delete("/api/visits/{id}")
    HttpResponse deleteVisit(@PathVariable Long id);
}
