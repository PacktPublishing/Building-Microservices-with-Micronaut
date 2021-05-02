package com.packtpub.micronaut.web.rest.client.petowner;

import com.packtpub.micronaut.service.dto.petowner.OwnerDTO;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller("/api")
public class OwnerResourceClientController {

    @Inject
    OwnerResourceClient ownerResourceClient;

    @NewSpan
    @Post("/owners")
    public HttpResponse<OwnerDTO> createOwner(@SpanTag("owner.dto") OwnerDTO ownerDTO) {
        return ownerResourceClient.createOwner(ownerDTO);
    }

    @NewSpan
    @Put("/owners")
    HttpResponse<OwnerDTO> updateOwner(@SpanTag("owner.dto") @Body OwnerDTO ownerDTO) {
        return ownerResourceClient.updateOwner(ownerDTO);
    }

    @NewSpan
    @Get("/owners")
    public HttpResponse<List<OwnerDTO>> getAllOwners(@SpanTag("http.request") HttpRequest request, @SpanTag("pageable") Pageable pageable) {
        return ownerResourceClient.getAllOwners(request, pageable);
    }

    @NewSpan
    @Get("/owners/{id}")
    public Optional<OwnerDTO> getOwner(@SpanTag("owner.id") @PathVariable Long id) {
        return ownerResourceClient.getOwner(id);
    }

    @NewSpan
    @Delete("/owners/{id}")
    public HttpResponse deleteOwner(@SpanTag("owner.id") @PathVariable Long id) {
        return ownerResourceClient.deleteOwner(id);
    }
}
