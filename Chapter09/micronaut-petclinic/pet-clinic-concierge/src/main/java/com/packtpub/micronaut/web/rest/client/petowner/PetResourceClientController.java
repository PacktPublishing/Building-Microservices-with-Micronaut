package com.packtpub.micronaut.web.rest.client.petowner;

import com.packtpub.micronaut.service.dto.petowner.PetDTO;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.tracing.annotation.NewSpan;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller("/api")
public class PetResourceClientController {

    @Inject
    PetResourceClient petResourceClient;

    @NewSpan
    @Post("/pets")
    public HttpResponse<PetDTO> createPet(PetDTO petDTO) {
        return petResourceClient.createPet(petDTO);
    }

    @NewSpan
    @Put("/pets")
    public HttpResponse<PetDTO> updatePet(PetDTO petDTO) {
        return petResourceClient.updatePet(petDTO);
    }

    @NewSpan
    @Get("/pets")
    public HttpResponse<List<PetDTO>> getAllPets(HttpRequest request, Pageable pageable) {
        return petResourceClient.getAllPets(request, pageable);
    }

    @NewSpan
    @Get("/pets/{id}")
    public Optional<PetDTO> getPet(Long id) {
        return petResourceClient.getPet(id);
    }

    @NewSpan
    @Delete("/pets/{id}")
    public HttpResponse deletePet(Long id) {
        return petResourceClient.deletePet(id);
    }
}
