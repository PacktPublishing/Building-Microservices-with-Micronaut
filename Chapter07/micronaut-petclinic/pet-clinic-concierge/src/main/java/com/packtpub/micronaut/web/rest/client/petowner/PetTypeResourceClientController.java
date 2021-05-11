package com.packtpub.micronaut.web.rest.client.petowner;

import com.packtpub.micronaut.service.dto.petowner.PetTypeDTO;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller("/api")
public class PetTypeResourceClientController {

    @Inject
    PetTypeResourceClient petTypeResourceClient;

    @Post("/pet-types")
    public HttpResponse<PetTypeDTO> createPetType(PetTypeDTO petTypeDTO) {
        return petTypeResourceClient.createPetType(petTypeDTO);
    }

    @Put("/pet-types")
    public HttpResponse<PetTypeDTO> updatePetType(PetTypeDTO petTypeDTO) {
        return petTypeResourceClient.updatePetType(petTypeDTO);
    }

    @Get("/pet-types")
    public HttpResponse<List<PetTypeDTO>> getAllPetTypes(HttpRequest request, Pageable pageable) {
        return petTypeResourceClient.getAllPetTypes(request, pageable);
    }

    @Get("/pet-types/{id}")
    public Optional<PetTypeDTO> getPetType(Long id) {
        return petTypeResourceClient.getPetType(id);
    }

    @Delete("/pet-types/{id}")
    public HttpResponse deletePetType(Long id) {
        return petTypeResourceClient.deletePetType(id);
    }
}
