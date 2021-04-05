package com.packtpub.micronaut.web.rest.client.petclinic;

import com.packtpub.micronaut.service.dto.petclinic.VetDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller("/api")
public class VetResourceClientController {

    @Inject
    VetResourceClient vetResourceClient;

    @Post("/vets")
    public HttpResponse<VetDTO> createVet(VetDTO vetDTO) {
        return vetResourceClient.createVet(vetDTO);
    }

    @Put("/vets")
    public HttpResponse<VetDTO> updateVet(VetDTO vetDTO) {
        return vetResourceClient.updateVet(vetDTO);
    }

    @Get("/vets")
    public HttpResponse<List<VetDTO>> getAllVets(HttpRequest request) {
        return vetResourceClient.getAllVets(request);
    }

    @Get("/vets/{id}")
    public Optional<VetDTO> getVet(Long id) {
        return vetResourceClient.getVet(id);
    }

    @Delete("/vets/{id}")
    public HttpResponse deleteVet(Long id) {
        return vetResourceClient.deleteVet(id);
    }
}
