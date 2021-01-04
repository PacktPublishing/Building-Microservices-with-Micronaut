package com.packtpub.micronaut.web.rest;

import com.packtpub.micronaut.service.VetService;
import com.packtpub.micronaut.service.dto.VetDTO;
import com.packtpub.micronaut.util.HeaderUtil;
import com.packtpub.micronaut.web.rest.errors.BadRequestAlertException;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.packtpub.micronaut.domain.Vet}.
 */
@Controller("/api")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class VetResource {

    private final Logger log = LoggerFactory.getLogger(VetResource.class);

    private static final String ENTITY_NAME = "vet";

    @Value("${micronaut.application.name}")
    private String applicationName;

    private final VetService vetService;

    public VetResource(VetService vetService) {
        this.vetService = vetService;
    }

    /**
     * {@code POST  /vets} : Create a new vet.
     *
     * @param vetDTO the vetDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new vetDTO, or with status {@code 400 (Bad Request)} if the vet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/vets")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<VetDTO> createVet(@Body VetDTO vetDTO) throws Exception {
        log.debug("REST request to save Vet : {}", vetDTO);
        if (vetDTO.getId() != null) {
            throw new BadRequestAlertException("A new vet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VetDTO result = vetService.save(vetDTO);
        URI location = new URI("/api/vets/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /vets} : Updates an existing vet.
     *
     * @param vetDTO the vetDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated vetDTO,
     * or with status {@code 400 (Bad Request)} if the vetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/vets")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<VetDTO> updateVet(@Body VetDTO vetDTO) throws Exception {
        log.debug("REST request to update Vet : {}", vetDTO);
        if (vetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VetDTO result = vetService.save(vetDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, vetDTO.getId().toString()));
    }

    /**
     * {@code GET  /vets} : get all the vets.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of vets in body.
     */
    @Get("/vets")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<VetDTO>> getAllVets(HttpRequest request) throws Exception {
        log.debug("REST request to get a page of Vets");
        List<VetDTO> list = (List<VetDTO>) vetService.findAll();
        return HttpResponse.ok(list);
    }

    /**
     * {@code GET  /vets/:id} : get the "id" vet.
     *
     * @param id the id of the vetDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the vetDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/vets/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<VetDTO> getVet(@PathVariable Long id) throws Exception {
        log.debug("REST request to get Vet : {}", id);
        
        return vetService.findOne(id);
    }

    /**
     * {@code DELETE  /vets/:id} : delete the "id" vet.
     *
     * @param id the id of the vetDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/vets/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteVet(@PathVariable Long id) throws Exception {
        log.debug("REST request to delete Vet : {}", id);
        vetService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
