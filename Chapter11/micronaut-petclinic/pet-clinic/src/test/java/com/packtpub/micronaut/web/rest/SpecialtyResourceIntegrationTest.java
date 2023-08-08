package com.packtpub.micronaut.web.rest;

import com.packtpub.micronaut.domain.Specialty;
import com.packtpub.micronaut.repository.SpecialtyRepository;
import com.packtpub.micronaut.service.dto.SpecialtyDTO;
import com.packtpub.micronaut.service.mapper.SpecialtyMapper;
import com.packtpub.micronaut.util.TestUtil;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link SpecialtyResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SpecialtyResourceIntegrationTest {

    private static final String DEFAULT_NAME = "FOO";
    private static final String UPDATED_NAME = "BAR";

    @Inject
    private SpecialtyMapper specialtyMapper;
    
    @Inject
    private SpecialtyRepository specialtyRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private Specialty specialty;

    @BeforeAll
    public void init() {
        specialty = new Specialty().name(DEFAULT_NAME);
    }

    @Test
    @Order(1)
    public void createSpecialty() throws Exception {
        int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // Create the Specialty
        HttpResponse<SpecialtyDTO> response = client.exchange(HttpRequest.POST("/api/specialties", specialtyDTO), SpecialtyDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = (List<Specialty>) specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeCreate + 1);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);

        // Set id for further tests
        specialty.setId(testSpecialty.getId());

        assertThat(testSpecialty.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Order(2)
    public void createSpecialtyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

        // Create the Specialty with an existing ID
        Specialty existingSpecialty = new Specialty();
        existingSpecialty.setId(1L);
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<SpecialtyDTO> response = client.exchange(HttpRequest.POST("/api/specialties", specialtyDTO), SpecialtyDTO.class)
            .onErrorReturn(t -> (HttpResponse<SpecialtyDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = (List<Specialty>) specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Order(3)
    public void getAllSpecialities() throws Exception {
        // Get the SpecialtyList w/ all the specialities
        List<SpecialtyDTO> specialities = client.retrieve(HttpRequest.GET("/api/specialties?eagerload=true"), Argument.listOf(SpecialtyDTO.class)).blockingFirst();
        SpecialtyDTO testSpecialty = specialities.get(specialities.size() - 1);

        assertThat(testSpecialty.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Order(4)
    public void getSpecialty() throws Exception {
        // Get the Specialty
        SpecialtyDTO testSpecialty = client.retrieve(HttpRequest.GET("/api/specialties/" + specialty.getId()), SpecialtyDTO.class).blockingFirst();

        assertThat(testSpecialty.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Order(5)
    public void getNonExistingSpecialty() throws Exception {
        // Get the Specialty
        @SuppressWarnings("unchecked")
        HttpResponse<SpecialtyDTO> response = client.exchange(HttpRequest.GET("/api/specialties/"+ Long.MAX_VALUE), SpecialtyDTO.class)
            .onErrorReturn(t -> (HttpResponse<SpecialtyDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    @Order(6)
    public void updateSpecialty() throws Exception {
        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // Update the Specialty
        Specialty updatedSpecialty = specialtyRepository.findById(specialty.getId());

        updatedSpecialty
            .name(UPDATED_NAME);
        SpecialtyDTO updatedSpecialtyDTO = specialtyMapper.toDto(updatedSpecialty);

        @SuppressWarnings("unchecked")
        HttpResponse<SpecialtyDTO> response = client.exchange(HttpRequest.PUT("/api/specialties", updatedSpecialtyDTO), SpecialtyDTO.class)
            .onErrorReturn(t -> (HttpResponse<SpecialtyDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = (List<Specialty>) specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);

        assertThat(testSpecialty.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Order(7)
    public void updateNonExistingSpecialty() throws Exception {
        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // Create the Specialty
        Specialty nonExistingSpecialty = new Specialty();
        nonExistingSpecialty.setId(null);
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(nonExistingSpecialty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<SpecialtyDTO> response = client.exchange(HttpRequest.PUT("/api/specialties", specialtyDTO), SpecialtyDTO.class)
            .onErrorReturn(t -> (HttpResponse<SpecialtyDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = (List<Specialty>) specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Order(8)
    public void deleteSpecialty() throws Exception {
        int databaseSizeBeforeDelete = specialtyRepository.findAll().size();

        // Delete the Specialty
        @SuppressWarnings("unchecked")
        HttpResponse<SpecialtyDTO> response = client.exchange(HttpRequest.DELETE("/api/specialties/"+ specialty.getId()), SpecialtyDTO.class)
            .onErrorReturn(t -> (HttpResponse<SpecialtyDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Specialty> specialtyList = (List<Specialty>) specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Order(9)
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Specialty.class);
        Specialty Specialty1 = new Specialty();
        Specialty1.setId(1L);
        Specialty Specialty2 = new Specialty();
        Specialty2.setId(Specialty1.getId());
        assertThat(Specialty1).isEqualTo(Specialty2);
        Specialty2.setId(2L);
        assertThat(Specialty1).isNotEqualTo(Specialty2);
        Specialty1.setId(null);
        assertThat(Specialty1).isNotEqualTo(Specialty2);
    }
}
