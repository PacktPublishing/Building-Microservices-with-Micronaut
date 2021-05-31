package com.packtpub.micronaut.web.rest.client.petclinic;

import com.packtpub.micronaut.service.dto.petclinic.VetDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller("/api")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class VetResourceClientController {

    @Inject
    VetResourceClient vetResourceClient;

    @NewSpan
    @Post("/vets")
    public HttpResponse<VetDTO> createVet(VetDTO vetDTO) {
        return vetResourceClient.createVet(vetDTO);
    }

    @NewSpan
    @Put("/vets")
    public HttpResponse<VetDTO> updateVet(VetDTO vetDTO) {
        return vetResourceClient.updateVet(vetDTO);
    }

    @NewSpan
    @Get("/vets")
    public HttpResponse<List<VetDTO>> getAllVets(HttpRequest request) {
        return vetResourceClient.getAllVets(request);
    }

    @NewSpan
    @Get("/vets/{id}")
    public Optional<VetDTO> getVet(Long id) {
        return vetResourceClient.getVet(id);
    }

    @NewSpan
    @Delete("/vets/{id}")
    public HttpResponse deleteVet(Long id) {
        return vetResourceClient.deleteVet(id);
    }
}
