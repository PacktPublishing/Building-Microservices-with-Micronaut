package com.packtpub.micronaut.web.rest.client.petowner;

import com.packtpub.micronaut.service.dto.petowner.PetDTO;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.retry.annotation.Fallback;

import java.util.List;
import java.util.Optional;

@Fallback
public class PetResourceFallback implements PetResourceClient {
    @Override
    public HttpResponse<PetDTO> createPet(PetDTO petDTO) {
        return HttpResponse.ok();
    }

    @Override
    public HttpResponse<PetDTO> updatePet(PetDTO petDTO) {
        return HttpResponse.ok();
    }

    @Override
    public HttpResponse<List<PetDTO>> getAllPets(HttpRequest request, Pageable pageable) {
        return HttpResponse.ok();
    }

    @Override
    public Optional<PetDTO> getPet(Long id) {
        return Optional.empty();
    }

    @Override
    public HttpResponse deletePet(Long id) {
        return HttpResponse.noContent();
    }
}
