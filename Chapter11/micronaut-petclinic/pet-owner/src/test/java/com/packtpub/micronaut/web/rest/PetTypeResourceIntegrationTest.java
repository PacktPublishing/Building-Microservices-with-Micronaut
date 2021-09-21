package com.packtpub.micronaut.web.rest;


import com.packtpub.micronaut.domain.PetType;
import com.packtpub.micronaut.repository.PetTypeRepository;
import com.packtpub.micronaut.service.dto.PetTypeDTO;
import com.packtpub.micronaut.service.mapper.PetTypeMapper;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link PetTypeResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PetTypeResourceIntegrationTest {

    private static final String DEFAULT_NAME = "FOO";
    private static final String UPDATED_NAME = "BAR";

    @Inject
    private PetTypeMapper petTypeMapper;

    @Inject
    private PetTypeRepository petTypeRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private PetType petType;

    @BeforeEach
    public void initTest() {
        petType = createEntity();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public PetType createEntity() {
        PetType petType = new PetType()
            .name(DEFAULT_NAME);
        return petType;
    }


    @Test
    public void createPetType() throws Exception {
        int databaseSizeBeforeCreate = petTypeRepository.findAll().size();

        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // Create the PetType
        HttpResponse<PetTypeDTO> response = client.exchange(HttpRequest.POST("/api/pet-types", petTypeDTO), PetTypeDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PetType testPetType = petTypeList.get(petTypeList.size() - 1);

        assertThat(testPetType.getName()).isEqualTo(DEFAULT_NAME);

        // Cleanup
        petType.setId(testPetType.getId());
        petTypeRepository.deleteById(petType.getId());
    }

    @Test
    public void createPetTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = petTypeRepository.findAll().size();

        // Create the PetType with an existing ID
        petType.setId(1L);
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<PetTypeDTO> response = client.exchange(HttpRequest.POST("/api/pet-types", petTypeDTO), PetTypeDTO.class)
            .onErrorReturn(t -> (HttpResponse<PetTypeDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllPetTypes() throws Exception {
        // Initialize the database with one entity
        PetType savedPetType = petTypeRepository.saveAndFlush(petType);
        petType.setId(savedPetType.getId());

        // Get the petTypeList w/ all the petTypes
        List<PetTypeDTO> petTypes = client.retrieve(HttpRequest.GET("/api/pet-types?eagerload=true"), Argument.listOf(PetTypeDTO.class)).blockingFirst();
        PetTypeDTO testPetType = petTypes.get(petTypes.size() - 1);

        assertThat(testPetType.getName()).isEqualTo(DEFAULT_NAME);

        // Cleanup
        petTypeRepository.deleteById(petType.getId());
    }

    @Test
    public void getPetType() throws Exception {
        // Initialize the database with one entity
        PetType savedPetType = petTypeRepository.saveAndFlush(petType);
        petType.setId(savedPetType.getId());

        // Get the petType
        PetTypeDTO testPetType = client.retrieve(HttpRequest.GET("/api/pet-types/" + petType.getId()), PetTypeDTO.class).blockingFirst();

        assertThat(testPetType.getName()).isEqualTo(DEFAULT_NAME);

        // Cleanup
        petTypeRepository.deleteById(petType.getId());
    }

    @Test
    public void getNonExistingPetType() throws Exception {
        // Get the petType
        @SuppressWarnings("unchecked")
        HttpResponse<PetTypeDTO> response = client.exchange(HttpRequest.GET("/api/pet-types/"+ Long.MAX_VALUE), PetTypeDTO.class)
            .onErrorReturn(t -> (HttpResponse<PetTypeDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updatePetType() throws Exception {
        // Initialize the database with one entity
        PetType savedPetType = petTypeRepository.saveAndFlush(petType);
        petType.setId(savedPetType.getId());

        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();

        // Update the petType
        PetType updatedPetType = petTypeRepository.findById(petType.getId()).get();

        updatedPetType
            .name(UPDATED_NAME);
        PetTypeDTO updatedPetTypeDTO = petTypeMapper.toDto(updatedPetType);

        @SuppressWarnings("unchecked")
        HttpResponse<PetTypeDTO> response = client.exchange(HttpRequest.PUT("/api/pet-types", updatedPetTypeDTO), PetTypeDTO.class)
            .onErrorReturn(t -> (HttpResponse<PetTypeDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
        PetType testPetType = petTypeList.get(petTypeList.size() - 1);

        assertThat(testPetType.getName()).isEqualTo(UPDATED_NAME);

        // Cleanup
        petTypeRepository.deleteById(petType.getId());
    }

    @Test
    public void updateNonExistingPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<PetTypeDTO> response = client.exchange(HttpRequest.PUT("/api/pet-types", petTypeDTO), PetTypeDTO.class)
            .onErrorReturn(t -> (HttpResponse<PetTypeDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePetType() throws Exception {
        // Initialize the database with one entity
        PetType savedPetType = petTypeRepository.saveAndFlush(petType);
        petType.setId(savedPetType.getId());

        int databaseSizeBeforeDelete = petTypeRepository.findAll().size();

        // Delete the petType
        @SuppressWarnings("unchecked")
        HttpResponse<PetTypeDTO> response = client.exchange(HttpRequest.DELETE("/api/pet-types/"+ petType.getId()), PetTypeDTO.class)
            .onErrorReturn(t -> (HttpResponse<PetTypeDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PetType.class);
        PetType petType1 = new PetType();
        petType1.setId(1L);
        PetType petType2 = new PetType();
        petType2.setId(petType1.getId());
        assertThat(petType1).isEqualTo(petType2);
        petType2.setId(2L);
        assertThat(petType1).isNotEqualTo(petType2);
        petType1.setId(null);
        assertThat(petType1).isNotEqualTo(petType2);
    }
}
