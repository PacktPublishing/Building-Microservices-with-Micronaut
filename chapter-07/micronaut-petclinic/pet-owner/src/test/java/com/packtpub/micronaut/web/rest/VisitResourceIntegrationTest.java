package com.packtpub.micronaut.web.rest;


import com.packtpub.micronaut.domain.Visit;
import com.packtpub.micronaut.repository.PetRepository;
import com.packtpub.micronaut.repository.VisitRepository;
import com.packtpub.micronaut.service.dto.VisitDTO;
import com.packtpub.micronaut.service.mapper.VisitMapper;
import com.packtpub.micronaut.util.TestUtil;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link VisitResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VisitResourceIntegrationTest {

    private static final LocalDate DEFAULT_VISIT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VISIT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "FOO";
    private static final String UPDATED_DESCRIPTION = "BAR";

    @Inject
    private VisitMapper visitMapper;

    @Inject
    private VisitRepository visitRepository;

    @Inject
    private PetRepository petRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private Visit visit;

    @BeforeEach
    public void initTest() {
        visit = createEntity();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public Visit createEntity() {
        Visit visit = new Visit()
            .visitDate(DEFAULT_VISIT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .pet(petRepository.findAll().get(0));
        return visit;
    }


    @Test
    public void createVisit() throws Exception {
        int databaseSizeBeforeCreate = visitRepository.findAll().size();

        VisitDTO visitDTO = visitMapper.toDto(visit);

        // Create the Visit
        HttpResponse<VisitDTO> response = client.exchange(HttpRequest.POST("/api/visits", visitDTO), VisitDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Visit in the database
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeCreate + 1);
        Visit testVisit = visitList.get(visitList.size() - 1);

        assertThat(testVisit.getVisitDate()).isEqualTo(DEFAULT_VISIT_DATE);
        assertThat(testVisit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Cleanup
        visit.setId(testVisit.getId());
        visitRepository.deleteById(visit.getId());
    }

    @Test
    public void createVisitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = visitRepository.findAll().size();

        // Create the Visit with an existing ID
        visit.setId(1L);
        VisitDTO visitDTO = visitMapper.toDto(visit);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<VisitDTO> response = client.exchange(HttpRequest.POST("/api/visits", visitDTO), VisitDTO.class)
            .onErrorReturn(t -> (HttpResponse<VisitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the Visit in the database
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllVisits() throws Exception {
        // Initialize the database
        Visit savedVisit = visitRepository.saveAndFlush(visit);
        visit.setId(savedVisit.getId());

        // Get the visitList w/ all the visits
        List<VisitDTO> visits = client.retrieve(HttpRequest.GET("/api/visits?eagerload=true"), Argument.listOf(VisitDTO.class)).blockingFirst();
        VisitDTO testVisit = visits.get(visits.size() - 1);

        assertThat(testVisit.getVisitDate()).isEqualTo(DEFAULT_VISIT_DATE);
        assertThat(testVisit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Cleanup
        visitRepository.deleteById(visit.getId());
    }

    @Test
    public void getVisit() throws Exception {
        // Initialize the database
        Visit savedVisit = visitRepository.saveAndFlush(visit);
        visit.setId(savedVisit.getId());

        // Get the visit
        VisitDTO testVisit = client.retrieve(HttpRequest.GET("/api/visits/" + visit.getId()), VisitDTO.class).blockingFirst();

        assertThat(testVisit.getVisitDate()).isEqualTo(DEFAULT_VISIT_DATE);
        assertThat(testVisit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Cleanup
        visitRepository.deleteById(visit.getId());
    }

    @Test
    public void getNonExistingVisit() throws Exception {
        // Get the visit
        @SuppressWarnings("unchecked")
        HttpResponse<VisitDTO> response = client.exchange(HttpRequest.GET("/api/visits/"+ Long.MAX_VALUE), VisitDTO.class)
            .onErrorReturn(t -> (HttpResponse<VisitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateVisit() throws Exception {
        // Initialize the database
        Visit savedVisit = visitRepository.saveAndFlush(visit);
        visit.setId(savedVisit.getId());

        int databaseSizeBeforeUpdate = visitRepository.findAll().size();

        // Update the visit
        Visit updatedVisit = visitRepository.findById(visit.getId()).get();

        updatedVisit
            .visitDate(UPDATED_VISIT_DATE)
            .description(UPDATED_DESCRIPTION);
        VisitDTO updatedVisitDTO = visitMapper.toDto(updatedVisit);

        @SuppressWarnings("unchecked")
        HttpResponse<VisitDTO> response = client.exchange(HttpRequest.PUT("/api/visits", updatedVisitDTO), VisitDTO.class)
            .onErrorReturn(t -> (HttpResponse<VisitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Visit in the database
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeUpdate);
        Visit testVisit = visitList.get(visitList.size() - 1);

        assertThat(testVisit.getVisitDate()).isEqualTo(UPDATED_VISIT_DATE);
        assertThat(testVisit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Cleanup
        visitRepository.deleteById(visit.getId());
    }

    @Test
    public void updateNonExistingVisit() throws Exception {
        int databaseSizeBeforeUpdate = visitRepository.findAll().size();

        // Create the Visit
        VisitDTO visitDTO = visitMapper.toDto(visit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<VisitDTO> response = client.exchange(HttpRequest.PUT("/api/visits", visitDTO), VisitDTO.class)
            .onErrorReturn(t -> (HttpResponse<VisitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the Visit in the database
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteVisit() throws Exception {
        // Initialize the database
        Visit savedVisit = visitRepository.saveAndFlush(visit);
        visit.setId(savedVisit.getId());

        int databaseSizeBeforeDelete = visitRepository.findAll().size();

        // Delete the visit
        @SuppressWarnings("unchecked")
        HttpResponse<VisitDTO> response = client.exchange(HttpRequest.DELETE("/api/visits/"+ visit.getId()), VisitDTO.class)
            .onErrorReturn(t -> (HttpResponse<VisitDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Visit> visitList = visitRepository.findAll();
        assertThat(visitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Visit.class);
        Visit visit1 = new Visit();
        visit1.setId(1L);
        Visit visit2 = new Visit();
        visit2.setId(visit1.getId());
        assertThat(visit1).isEqualTo(visit2);
        visit2.setId(2L);
        assertThat(visit1).isNotEqualTo(visit2);
        visit1.setId(null);
        assertThat(visit1).isNotEqualTo(visit2);
    }
}
