package com.packtpub.micronaut.web.rest;


import com.packtpub.micronaut.domain.Vet;
import com.packtpub.micronaut.repository.SpecialtyRepository;
import com.packtpub.micronaut.repository.VetRepository;
import com.packtpub.micronaut.service.dto.VetDTO;
import com.packtpub.micronaut.service.mapper.VetMapper;
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
 * Integration tests for the {@Link VetResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VetResourceIntegrationTest {

    private static final String DEFAULT_FIRST_NAME = "FOO";
    private static final String UPDATED_FIRST_NAME = "BAR";

    private static final String DEFAULT_LAST_NAME = "FOO";
    private static final String UPDATED_LAST_NAME = "BAR";

    @Inject
    private VetMapper vetMapper;

    @Inject
    private VetRepository vetRepository;

    @Inject
    private SpecialtyRepository specialtyRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private Vet vet;

    @BeforeAll
    public void initTest() throws Exception {
        vet = createEntity();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public Vet createEntity() throws Exception {
        Vet vet = new Vet()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .addSpecialty(specialtyRepository.findAll().stream().findFirst().orElse(null));
        return vet;
    }

    @Test
    @Order(1)
    public void createVet() throws Exception {
        int databaseSizeBeforeCreate = vetRepository.findAll().size();

        VetDTO vetDTO = vetMapper.toDto(vet);

        // Create the Vet
        HttpResponse<VetDTO> response = client.exchange(HttpRequest.POST("/api/vets", vetDTO), VetDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Vet in the database
        List<Vet> vetList = (List<Vet>) vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeCreate + 1);
        Vet testVet = vetList.get(vetList.size() - 1);

        // Set id for further tests
        vet.setId(testVet.getId());

        assertThat(testVet.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testVet.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Order(2)
    public void createVetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vetRepository.findAll().size();

        // Create the Vet with an existing ID
        Vet existingVet = new Vet();
        existingVet.setId(1L);
        VetDTO vetDTO = vetMapper.toDto(vet);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<VetDTO> response = client.exchange(HttpRequest.POST("/api/vets", vetDTO), VetDTO.class)
            .onErrorReturn(t -> (HttpResponse<VetDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the Vet in the database
        List<Vet> vetList = (List<Vet>) vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Order(3)
    public void getAllVets() throws Exception {
        // Get the vetList w/ all the vets
        List<VetDTO> vets = client.retrieve(HttpRequest.GET("/api/vets?eagerload=true"), Argument.listOf(VetDTO.class)).blockingFirst();
        VetDTO testVet = vets.get(vets.size() - 1);

        assertThat(testVet.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testVet.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Order(4)
    public void getVet() throws Exception {
        // Get the vet
        VetDTO testVet = client.retrieve(HttpRequest.GET("/api/vets/" + vet.getId()), VetDTO.class).blockingFirst();

        assertThat(testVet.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testVet.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Order(5)
    public void getNonExistingVet() throws Exception {
        // Get the vet
        @SuppressWarnings("unchecked")
        HttpResponse<VetDTO> response = client.exchange(HttpRequest.GET("/api/vets/"+ Long.MAX_VALUE), VetDTO.class)
            .onErrorReturn(t -> (HttpResponse<VetDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    @Order(6)
    public void updateVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();

        // Update the vet
        Vet updatedVet = vetRepository.findById(vet.getId());

        updatedVet
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);
        VetDTO updatedVetDTO = vetMapper.toDto(updatedVet);

        @SuppressWarnings("unchecked")
        HttpResponse<VetDTO> response = client.exchange(HttpRequest.PUT("/api/vets", updatedVetDTO), VetDTO.class)
            .onErrorReturn(t -> (HttpResponse<VetDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Vet in the database
        List<Vet> vetList = (List<Vet>) vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
        Vet testVet = vetList.get(vetList.size() - 1);

        assertThat(testVet.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testVet.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Order(7)
    public void updateNonExistingVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();

        // Create the Vet
        Vet nonExistingVet = new Vet();
        nonExistingVet.setId(null);
        VetDTO vetDTO = vetMapper.toDto(nonExistingVet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<VetDTO> response = client.exchange(HttpRequest.PUT("/api/vets", vetDTO), VetDTO.class)
            .onErrorReturn(t -> (HttpResponse<VetDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the Vet in the database
        List<Vet> vetList = (List<Vet>) vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Order(8)
    public void deleteVet() throws Exception {
        int databaseSizeBeforeDelete = vetRepository.findAll().size();

        // Delete the vet
        @SuppressWarnings("unchecked")
        HttpResponse<VetDTO> response = client.exchange(HttpRequest.DELETE("/api/vets/"+ vet.getId()), VetDTO.class)
            .onErrorReturn(t -> (HttpResponse<VetDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Vet> vetList = (List<Vet>) vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Order(9)
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vet.class);
        Vet vet1 = new Vet();
        vet1.setId(1L);
        Vet vet2 = new Vet();
        vet2.setId(vet1.getId());
        assertThat(vet1).isEqualTo(vet2);
        vet2.setId(2L);
        assertThat(vet1).isNotEqualTo(vet2);
        vet1.setId(null);
        assertThat(vet1).isNotEqualTo(vet2);
    }
}
