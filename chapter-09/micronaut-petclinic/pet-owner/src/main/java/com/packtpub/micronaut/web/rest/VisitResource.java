package com.packtpub.micronaut.web.rest;

import com.packtpub.micronaut.service.VisitService;
import com.packtpub.micronaut.service.dto.VisitDTO;
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
import io.micronaut.tracing.annotation.ContinueSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.packtpub.micronaut.domain.Visit}.
 */
@Controller("/api")
public class VisitResource {

    private final Logger log = LoggerFactory.getLogger(VisitResource.class);

    private static final String ENTITY_NAME = "visit";

    @Value("${micronaut.application.name}")
    private String applicationName;

    private final VisitService visitService;

    public VisitResource(VisitService visitService) {
        this.visitService = visitService;
    }

    /**
     * {@code POST  /visits} : Create a new visit.
     *
     * @param visitDTO the visitDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new visitDTO, or with status {@code 400 (Bad Request)} if the visit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/visits")
    @ExecuteOn(TaskExecutors.IO)
    @ContinueSpan
    public HttpResponse<VisitDTO> createVisit(@Body VisitDTO visitDTO) throws URISyntaxException {
        log.debug("REST request to save Visit : {}", visitDTO);
        if (visitDTO.getId() != null) {
            throw new BadRequestAlertException("A new visit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VisitDTO result = visitService.save(visitDTO);
        URI location = new URI("/api/visits/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /visits} : Updates an existing visit.
     *
     * @param visitDTO the visitDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated visitDTO,
     * or with status {@code 400 (Bad Request)} if the visitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the visitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/visits")
    @ExecuteOn(TaskExecutors.IO)
    @ContinueSpan
    public HttpResponse<VisitDTO> updateVisit(@Body VisitDTO visitDTO) throws URISyntaxException {
        log.debug("REST request to update Visit : {}", visitDTO);
        if (visitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VisitDTO result = visitService.save(visitDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, visitDTO.getId().toString()));
    }

    /**
     * {@code GET  /visits} : get all the visits.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of visits in body.
     */
    @Get("/visits")
    @ExecuteOn(TaskExecutors.IO)
    @ContinueSpan
    public HttpResponse<List<VisitDTO>> getAllVisits(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of Visits");
        Page<VisitDTO> page = visitService.findAll(pageable);
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /visits/:id} : get the "id" visit.
     *
     * @param id the id of the visitDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the visitDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/visits/{id}")
    @ExecuteOn(TaskExecutors.IO)
    @ContinueSpan
    public Optional<VisitDTO> getVisit(@PathVariable Long id) {
        log.debug("REST request to get Visit : {}", id);
        
        return visitService.findOne(id);
    }

    /**
     * {@code DELETE  /visits/:id} : delete the "id" visit.
     *
     * @param id the id of the visitDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/visits/{id}")
    @ExecuteOn(TaskExecutors.IO)
    @ContinueSpan
    public HttpResponse deleteVisit(@PathVariable Long id) {
        log.debug("REST request to delete Visit : {}", id);
        visitService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
