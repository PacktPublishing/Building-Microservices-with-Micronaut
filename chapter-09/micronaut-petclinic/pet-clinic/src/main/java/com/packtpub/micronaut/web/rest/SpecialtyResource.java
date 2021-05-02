package com.packtpub.micronaut.web.rest;

import com.packtpub.micronaut.service.SpecialtyService;
import com.packtpub.micronaut.service.dto.SpecialtyDTO;
import com.packtpub.micronaut.util.HeaderUtil;
import com.packtpub.micronaut.web.rest.errors.BadRequestAlertException;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.packtpub.micronaut.domain.Specialty}.
 */
@Controller("/api")
public class SpecialtyResource {

    private final Logger log = LoggerFactory.getLogger(SpecialtyResource.class);

    private static final String ENTITY_NAME = "specialty";

    @Value("${micronaut.application.name}")
    private String applicationName;

    private final SpecialtyService specialtyService;

    public SpecialtyResource(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    /**
     * {@code POST  /specialties} : Create a new specialty.
     *
     * @param specialtyDTO the specialtyDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new specialtyDTO, or with status {@code 400 (Bad Request)} if the specialty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/specialties")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<SpecialtyDTO> createSpecialty(@Body SpecialtyDTO specialtyDTO) throws Exception {
        log.debug("REST request to save Specialty : {}", specialtyDTO);
        if (specialtyDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecialtyDTO result = specialtyService.save(specialtyDTO);
        URI location = new URI("/api/specialties/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /specialties} : Updates an existing specialty.
     *
     * @param specialtyDTO the specialtyDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated specialtyDTO,
     * or with status {@code 400 (Bad Request)} if the specialtyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialtyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/specialties")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<SpecialtyDTO> updateSpecialty(@Body SpecialtyDTO specialtyDTO) throws Exception {
        log.debug("REST request to update Specialty : {}", specialtyDTO);
        if (specialtyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SpecialtyDTO result = specialtyService.save(specialtyDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, specialtyDTO.getId().toString()));
    }

    /**
     * {@code GET  /specialties} : get all the specialties.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of specialties in body.
     */
    @Get("/specialties")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<SpecialtyDTO>> getAllSpecialties(HttpRequest request) throws Exception {
        log.debug("REST request to get a page of Specialties");
        List<SpecialtyDTO> list = (List<SpecialtyDTO>) specialtyService.findAll();
        return HttpResponse.ok(list);
    }

    /**
     * {@code GET  /specialties/:id} : get the "id" specialty.
     *
     * @param id the id of the specialtyDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the specialtyDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/specialties/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<SpecialtyDTO> getSpecialty(@PathVariable Long id) throws Exception {
        log.debug("REST request to get Specialty : {}", id);
        
        return specialtyService.findOne(id);
    }

    /**
     * {@code DELETE  /specialties/:id} : delete the "id" specialty.
     *
     * @param id the id of the specialtyDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/specialties/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteSpecialty(@PathVariable Long id) throws Exception {
        log.debug("REST request to delete Specialty : {}", id);
        specialtyService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
