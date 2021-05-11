package com.packtpub.micronaut.web.rest;

import com.packtpub.micronaut.service.OwnerService;
import com.packtpub.micronaut.service.dto.OwnerDTO;
import com.packtpub.micronaut.util.HeaderUtil;
import com.packtpub.micronaut.util.PaginationUtil;
import com.packtpub.micronaut.web.rest.errors.BadRequestAlertException;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.version.annotation.Version;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.packtpub.micronaut.domain.Owner}.
 */
@Controller("/api")
public class OwnerResource {

    private final Logger log = LoggerFactory.getLogger(OwnerResource.class);

    private static final String ENTITY_NAME = "owner";

    @Value("${micronaut.application.name}")
    private String applicationName;

    private final OwnerService ownerService;

    public OwnerResource(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * {@code POST  /owners} : Create a new owner.
     *
     * @param ownerDTO the ownerDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new ownerDTO, or with status {@code 400 (Bad Request)} if the owner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/owners")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<OwnerDTO> createOwner(@Body OwnerDTO ownerDTO) throws URISyntaxException {
        log.debug("REST request to save Owner : {}", ownerDTO);
        if (ownerDTO.getId() != null) {
            throw new BadRequestAlertException("A new owner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OwnerDTO result = ownerService.save(ownerDTO);
        URI location = new URI("/api/owners/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /owners} : Updates an existing owner.
     *
     * @param ownerDTO the ownerDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated ownerDTO,
     * or with status {@code 400 (Bad Request)} if the ownerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ownerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/owners")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<OwnerDTO> updateOwner(@Body OwnerDTO ownerDTO) throws URISyntaxException {
        log.debug("REST request to update Owner : {}", ownerDTO);
        if (ownerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OwnerDTO result = ownerService.save(ownerDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, ownerDTO.getId().toString()));
    }

    /**
     * {@code GET  /owners} : get all the owners.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of owners in body.
     */
    @Get("/owners")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<OwnerDTO>> getAllOwners(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of Owners");
        Page<OwnerDTO> page = ownerService.findAll(pageable);
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /owners/:id} : get the "id" owner.
     *
     * @param id the id of the ownerDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the ownerDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/owners/{id}")
    @Version("1")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<OwnerDTO> getOwner(@PathVariable Long id) {
        log.debug("REST request to get Owner : {}", id);
        return ownerService.findOne(id);
    }

    /**
     * {@code GET  /owners/:id} : get the "id" owner.
     *
     * @param id the id of the ownerDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the ownerDTO, or with status {@code 404 (Not Found)}.
     */
    @Version("2")
    @Get("/owners/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<OwnerDTO> getOwnerV2(@PathVariable Long id) {
        log.debug("REST request to get Owner : {}", id);
        return ownerService.findOne(id);
    }

    /**
     * {@code DELETE  /owners/:id} : delete the "id" owner.
     *
     * @param id the id of the ownerDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/owners/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteOwner(@PathVariable Long id) {
        log.debug("REST request to delete Owner : {}", id);
        ownerService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
