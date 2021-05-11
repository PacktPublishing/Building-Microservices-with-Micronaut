package com.packtpub.micronaut.web.rest.client.petowner;

import com.packtpub.micronaut.service.dto.petowner.VisitDTO;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.tracing.annotation.NewSpan;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller("/api")
public class VisitResourceClientController {

    @Inject
    VisitResourceClient visitResourceClient;

    @NewSpan
    @Post("/visits")
    public HttpResponse<VisitDTO> createVisit(VisitDTO visitDTO) {
        return visitResourceClient.createVisit(visitDTO);
    }

    @NewSpan
    @Put("/visits")
    public HttpResponse<VisitDTO> updateVisit(VisitDTO visitDTO) {
        return visitResourceClient.updateVisit(visitDTO);
    }

    @NewSpan
    @Get("/visits")
    public HttpResponse<List<VisitDTO>> getAllVisits(HttpRequest request, Pageable pageable) {
        return visitResourceClient.getAllVisits(request, pageable);
    }

    @NewSpan
    @Get("/visits/{id}")
    public Optional<VisitDTO> getVisit(Long id) {
        return visitResourceClient.getVisit(id);
    }

    @NewSpan
    @Delete("/visits/{id}")
    public HttpResponse deleteVisit(Long id) {
        return visitResourceClient.deleteVisit(id);
    }
}
