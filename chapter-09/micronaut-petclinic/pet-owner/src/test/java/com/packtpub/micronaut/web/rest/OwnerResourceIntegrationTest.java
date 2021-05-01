package com.packtpub.micronaut.web.rest;


import com.packtpub.micronaut.domain.Owner;
import com.packtpub.micronaut.repository.OwnerRepository;
import com.packtpub.micronaut.service.dto.OwnerDTO;
import com.packtpub.micronaut.service.mapper.OwnerMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link OwnerResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OwnerResourceIntegrationTest {

    private static final String DEFAULT_FIRST_NAME = "FOO";
    private static final String UPDATED_FIRST_NAME = "BAR";

    private static final String DEFAULT_LAST_NAME = "FOO";
    private static final String UPDATED_LAST_NAME = "BAR";

    private static final String DEFAULT_ADDRESS = "FOO";
    private static final String UPDATED_ADDRESS = "BAR";

    private static final String DEFAULT_CITY = "FOO";
    private static final String UPDATED_CITY = "BAR";

    private static final String DEFAULT_TELEPHONE = "FOO";
    private static final String UPDATED_TELEPHONE = "BAR";

    @Inject
    private OwnerMapper ownerMapper;

    @Inject
    private OwnerRepository ownerRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private Owner owner;

    @BeforeEach
    public void initTest() {
        owner = createEntity();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public Owner createEntity() {
        Owner owner = new Owner()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .telephone(DEFAULT_TELEPHONE);
        return owner;
    }

    @Test
    public void createOwner() throws Exception {
        // Create the Owner
        int databaseSizeBeforeCreate = ownerRepository.findAll().size();
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        HttpResponse<OwnerDTO> response = client.exchange(HttpRequest.POST("/api/owners", ownerDTO), OwnerDTO.class).blockingFirst();
        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Owner
        List<Owner> ownerList = ownerRepository.findAll();
        Owner testOwner = ownerList.get(ownerList.size() - 1);

        assertThat(ownerList).hasSize(databaseSizeBeforeCreate + 1);
        assertThat(testOwner.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testOwner.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testOwner.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOwner.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testOwner.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);

        // Cleanup
        owner.setId(testOwner.getId());
        ownerRepository.deleteById(owner.getId());
    }

    @Test
    public void createOwnerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ownerRepository.findAll().size();

        // Create the Owner with an existing ID
        owner.setId(1L);
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<OwnerDTO> response = client.exchange(HttpRequest.POST("/api/owners", ownerDTO), OwnerDTO.class)
            .onErrorReturn(t -> (HttpResponse<OwnerDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllOwners() throws Exception {
        // Initialize the database
        Owner savedOwner = ownerRepository.saveAndFlush(owner);
        owner.setId(savedOwner.getId());

        // Get the ownerList w/ all the owners
        List<OwnerDTO> owners = client.retrieve(HttpRequest.GET("/api/owners?eagerload=true"), Argument.listOf(OwnerDTO.class)).blockingFirst();
        OwnerDTO testOwner = owners.get(owners.size() - 1);

        // Verify owners
        assertThat(testOwner.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testOwner.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testOwner.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOwner.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testOwner.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);

        // Cleanup
        ownerRepository.deleteById(owner.getId());
    }

    @Test
    public void getOwner() throws Exception {
        // Initialize the database
        Owner savedOwner = ownerRepository.saveAndFlush(owner);
        owner.setId(savedOwner.getId());

        // Get the owner
        OwnerDTO testOwner = client.retrieve(HttpRequest.GET("/api/owners/" + owner.getId()), OwnerDTO.class).blockingFirst();

        // Verify owner
        assertThat(testOwner.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testOwner.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testOwner.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOwner.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testOwner.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);

        // Cleanup
        ownerRepository.deleteById(owner.getId());
    }

    @Test
    public void getNonExistingOwner() throws Exception {
        // Get the owner
        @SuppressWarnings("unchecked")
        HttpResponse<OwnerDTO> response = client.exchange(HttpRequest.GET("/api/owners/"+ Long.MAX_VALUE), OwnerDTO.class)
            .onErrorReturn(t -> (HttpResponse<OwnerDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateOwner() throws Exception {
        // Initialize the database
        Owner savedOwner = ownerRepository.saveAndFlush(owner);
        owner.setId(savedOwner.getId());

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner
        Owner updatedOwner = ownerRepository.findById(owner.getId()).get();

        updatedOwner
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .telephone(UPDATED_TELEPHONE);
        OwnerDTO updatedOwnerDTO = ownerMapper.toDto(updatedOwner);

        @SuppressWarnings("unchecked")
        HttpResponse<OwnerDTO> response = client.exchange(HttpRequest.PUT("/api/owners", updatedOwnerDTO), OwnerDTO.class)
            .onErrorReturn(t -> (HttpResponse<OwnerDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);

        assertThat(testOwner.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testOwner.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testOwner.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOwner.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testOwner.getTelephone()).isEqualTo(UPDATED_TELEPHONE);

        // Cleanup
        ownerRepository.deleteById(owner.getId());
    }

    @Test
    public void updateNonExistingOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<OwnerDTO> response = client.exchange(HttpRequest.PUT("/api/owners", ownerDTO), OwnerDTO.class)
            .onErrorReturn(t -> (HttpResponse<OwnerDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteOwner() throws Exception {
        // Initialize the database with one entity
        Owner savedOwner = ownerRepository.saveAndFlush(owner);
        owner.setId(savedOwner.getId());

        int databaseSizeBeforeDelete = ownerRepository.findAll().size();

        // Delete the owner
        @SuppressWarnings("unchecked")
        HttpResponse<OwnerDTO> response = client.exchange(HttpRequest.DELETE("/api/owners/"+ owner.getId()), OwnerDTO.class)
            .onErrorReturn(t -> (HttpResponse<OwnerDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Owner.class);
        Owner owner1 = new Owner();
        owner1.setId(1L);
        Owner owner2 = new Owner();
        owner2.setId(owner1.getId());
        assertThat(owner1).isEqualTo(owner2);
        owner2.setId(2L);
        assertThat(owner1).isNotEqualTo(owner2);
        owner1.setId(null);
        assertThat(owner1).isNotEqualTo(owner2);
    }
}
