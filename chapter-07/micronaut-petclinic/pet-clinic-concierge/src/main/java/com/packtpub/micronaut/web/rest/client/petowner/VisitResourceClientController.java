package com.packtpub.micronaut.web.rest.client.petowner;

import com.packtpub.micronaut.service.dto.petowner.VisitDTO;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller("/api")
public class VisitResourceClientController {

    @Inject
    VisitResourceClient visitResourceClient;

    @Post("/visits")
    public HttpResponse<VisitDTO> createVisit(VisitDTO visitDTO) {
        return visitResourceClient.createVisit(visitDTO);
    }

    @Put("/visits")
    public HttpResponse<VisitDTO> updateVisit(VisitDTO visitDTO) {
        return visitResourceClient.updateVisit(visitDTO);
    }

    @Get("/visits")
    public HttpResponse<List<VisitDTO>> getAllVisits(HttpRequest request, Pageable pageable) {
        return visitResourceClient.getAllVisits(request, pageable);
    }

    @Get("/visits/{id}")
    public Optional<VisitDTO> getVisit(Long id) {
        return visitResourceClient.getVisit(id);
    }

    @Delete("/visits/{id}")
    public HttpResponse deleteVisit(Long id) {
        return visitResourceClient.deleteVisit(id);
    }
}
