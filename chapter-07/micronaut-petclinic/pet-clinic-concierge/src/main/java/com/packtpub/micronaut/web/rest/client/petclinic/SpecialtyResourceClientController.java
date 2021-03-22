package com.packtpub.micronaut.web.rest.client.petclinic;

import com.packtpub.micronaut.service.dto.petclinic.SpecialtyDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller("/api")
public class SpecialtyResourceClientController {

    @Inject
    SpecialtyResourceClient specialtyResourceClient;

    @Post("/specialties")
    public HttpResponse<SpecialtyDTO> createSpecialty(SpecialtyDTO specialtyDTO) {
        return specialtyResourceClient.createSpecialty(specialtyDTO);
    }

    @Put("/specialties")
    public HttpResponse<SpecialtyDTO> updateSpecialty(SpecialtyDTO specialtyDTO) {
        return specialtyResourceClient.updateSpecialty(specialtyDTO);
    }

    @Get("/specialties")
    public HttpResponse<List<SpecialtyDTO>> getAllSpecialties(HttpRequest request) {
        return specialtyResourceClient.getAllSpecialties(request);
    }

    @Get("/specialties/{id}")
    public Optional<SpecialtyDTO> getSpecialty(Long id) {
        return specialtyResourceClient.getSpecialty(id);
    }

    @Delete("/specialties/{id}")
    public HttpResponse deleteSpecialty(Long id) {
        return specialtyResourceClient.deleteSpecialty(id);
    }
}
