package com.packtpub.micronaut.web.rest;

import com.packtpub.micronaut.service.PetTypeService;
import com.packtpub.micronaut.service.dto.PetTypeDTO;
import com.packtpub.micronaut.util.HeaderUtil;
import com.packtpub.micronaut.util.PaginationUtil;
import com.packtpub.micronaut.web.rest.errors.BadRequestAlertException;
import io.micronaut.context.annotation.Value;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.ContinueSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.packtpub.micronaut.domain.PetType}.
 */
@Controller("/api")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class PetTypeResource {

    private final Logger log = LoggerFactory.getLogger(PetTypeResource.class);

    private static final String ENTITY_NAME = "petType";

    @Value("${micronaut.application.name}")
    private String applicationName;

    private final PetTypeService petTypeService;

    public PetTypeResource(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    /**
     * {@code POST  /pet-types} : Create a new petType.
     *
     * @param petTypeDTO the petTypeDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new petTypeDTO, or with status {@code 400 (Bad Request)} if the petType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/pet-types")
    @ExecuteOn(TaskExecutors.IO)
    @ContinueSpan
    public HttpResponse<PetTypeDTO> createPetType(@Body PetTypeDTO petTypeDTO) throws URISyntaxException {
        log.debug("REST request to save PetType : {}", petTypeDTO);
        if (petTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new petType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PetTypeDTO result = petTypeService.save(petTypeDTO);
        URI location = new URI("/api/pet-types/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /pet-types} : Updates an existing petType.
     *
     * @param petTypeDTO the petTypeDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated petTypeDTO,
     * or with status {@code 400 (Bad Request)} if the petTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the petTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/pet-types")
    @ExecuteOn(TaskExecutors.IO)
    @ContinueSpan
    public HttpResponse<PetTypeDTO> updatePetType(@Body PetTypeDTO petTypeDTO) throws URISyntaxException {
        log.debug("REST request to update PetType : {}", petTypeDTO);
        if (petTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PetTypeDTO result = petTypeService.save(petTypeDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, petTypeDTO.getId().toString()));
    }

    /**
     * {@code GET  /pet-types} : get all the petTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of petTypes in body.
     */
    @Get("/pet-types")
    @ExecuteOn(TaskExecutors.IO)
    @ContinueSpan
    public HttpResponse<List<PetTypeDTO>> getAllPetTypes(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of PetTypes");
        Page<PetTypeDTO> page = petTypeService.findAll(pageable);
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /pet-types/:id} : get the "id" petType.
     *
     * @param id the id of the petTypeDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the petTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/pet-types/{id}")
    @ExecuteOn(TaskExecutors.IO)
    @ContinueSpan
    public Optional<PetTypeDTO> getPetType(@PathVariable Long id) {
        log.debug("REST request to get PetType : {}", id);
        
        return petTypeService.findOne(id);
    }

    /**
     * {@code DELETE  /pet-types/:id} : delete the "id" petType.
     *
     * @param id the id of the petTypeDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/pet-types/{id}")
    @ExecuteOn(TaskExecutors.IO)
    @ContinueSpan
    public HttpResponse deletePetType(@PathVariable Long id) {
        log.debug("REST request to delete PetType : {}", id);
        petTypeService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
