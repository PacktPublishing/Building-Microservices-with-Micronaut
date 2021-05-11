package com.packtpub.micronaut.web.rest.client.petclinic;

import com.packtpub.micronaut.service.dto.petclinic.SpecialtyDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

import java.util.List;
import java.util.Optional;

@Client(id = "pet-clinic")
public interface SpecialtyResourceClient {

    @Post("/api/specialties")
    HttpResponse<SpecialtyDTO> createSpecialty(@Body SpecialtyDTO specialtyDTO);

    @Put("/api/specialties")
    HttpResponse<SpecialtyDTO> updateSpecialty(@Body SpecialtyDTO specialtyDTO);

    @Get("/api/specialties")
    HttpResponse<List<SpecialtyDTO>> getAllSpecialties(HttpRequest request);

    @Get("/api/specialties/{id}")
    Optional<SpecialtyDTO> getSpecialty(@PathVariable Long id);

    @Delete("/api/specialties/{id}")
    HttpResponse deleteSpecialty(@PathVariable Long id);
}
