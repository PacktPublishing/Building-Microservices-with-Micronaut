package com.packtpub.micronaut.web.rest;


import com.packtpub.micronaut.domain.Pet;
import com.packtpub.micronaut.repository.OwnerRepository;
import com.packtpub.micronaut.repository.PetRepository;
import com.packtpub.micronaut.repository.PetTypeRepository;
import com.packtpub.micronaut.service.dto.PetDTO;
import com.packtpub.micronaut.service.mapper.PetMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link PetResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PetResourceIntegrationTest {

    private static final String DEFAULT_NAME = "FOO";
    private static final String UPDATED_NAME = "BAR";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private PetMapper petMapper;

    @Inject
    private PetRepository petRepository;

    @Inject
    private PetTypeRepository petTypeRepository;

    @Inject
    private OwnerRepository ownerRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private Pet pet;

    @BeforeEach
    public void initTest() {
        pet = createEntity();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public Pet createEntity() {
        Pet pet = new Pet()
            .name(DEFAULT_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .type(petTypeRepository.findAll().get(0))
            .owner(ownerRepository.findAll().get(0));
        return pet;
    }

    @Test
    public void createPet() throws Exception {
        int databaseSizeBeforeCreate = petRepository.findAll().size();

        PetDTO petDTO = petMapper.toDto(pet);

        // Create the Pet
        HttpResponse<PetDTO> response = client.exchange(HttpRequest.POST("/api/pets", petDTO), PetDTO.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeCreate + 1);
        Pet testPet = petList.get(petList.size() - 1);

        assertThat(testPet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPet.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);

        // Cleanup
        pet.setId(testPet.getId());
        petRepository.deleteById(pet.getId());
    }

    @Test
    public void createPetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = petRepository.findAll().size();

        // Create the Pet with an existing ID
        pet.setId(1L);
        PetDTO petDTO = petMapper.toDto(pet);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<PetDTO> response = client.exchange(HttpRequest.POST("/api/pets", petDTO), PetDTO.class)
            .onErrorReturn(t -> (HttpResponse<PetDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllPets() throws Exception {
        // Initialize the database
        Pet savedPet = petRepository.saveAndFlush(pet);
        pet.setId(savedPet.getId());

        // Get the petList w/ all the pets
        List<PetDTO> pets = client.retrieve(HttpRequest.GET("/api/pets?eagerload=true"), Argument.listOf(PetDTO.class)).blockingFirst();
        PetDTO testPet = pets.get(pets.size() - 1);

        assertThat(testPet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPet.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);

        // Cleanup
        petRepository.deleteById(pet.getId());
    }

    @Test
    public void getPet() throws Exception {
        // Initialize the database
        Pet savedPet = petRepository.saveAndFlush(pet);
        pet.setId(savedPet.getId());

        // Get the pet
        PetDTO testPet = client.retrieve(HttpRequest.GET("/api/pets/" + pet.getId()), PetDTO.class).blockingFirst();

        assertThat(testPet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPet.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);

        // Cleanup
        petRepository.deleteById(pet.getId());
    }

    @Test
    public void getNonExistingPet() throws Exception {
        // Get the pet
        @SuppressWarnings("unchecked")
        HttpResponse<PetDTO> response = client.exchange(HttpRequest.GET("/api/pets/"+ Long.MAX_VALUE), PetDTO.class)
            .onErrorReturn(t -> (HttpResponse<PetDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updatePet() throws Exception {
        // Initialize the database
        Pet savedPet = petRepository.saveAndFlush(pet);
        pet.setId(savedPet.getId());

        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet
        Pet updatedPet = petRepository.findById(pet.getId()).get();

        updatedPet
            .name(UPDATED_NAME)
            .birthDate(UPDATED_BIRTH_DATE);
        PetDTO updatedPetDTO = petMapper.toDto(updatedPet);

        @SuppressWarnings("unchecked")
        HttpResponse<PetDTO> response = client.exchange(HttpRequest.PUT("/api/pets", updatedPetDTO), PetDTO.class)
            .onErrorReturn(t -> (HttpResponse<PetDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = petList.get(petList.size() - 1);

        assertThat(testPet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPet.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);

        // Cleanup
        petRepository.deleteById(pet.getId());
    }

    @Test
    public void updateNonExistingPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<PetDTO> response = client.exchange(HttpRequest.PUT("/api/pets", petDTO), PetDTO.class)
            .onErrorReturn(t -> (HttpResponse<PetDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePet() throws Exception {
        // Initialize the database
        Pet savedPet = petRepository.saveAndFlush(pet);
        pet.setId(savedPet.getId());

        int databaseSizeBeforeDelete = petRepository.findAll().size();

        // Delete the pet
        @SuppressWarnings("unchecked")
        HttpResponse<PetDTO> response = client.exchange(HttpRequest.DELETE("/api/pets/"+ pet.getId()), PetDTO.class)
            .onErrorReturn(t -> (HttpResponse<PetDTO>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        // Validate the database is now empty
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pet.class);
        Pet pet1 = new Pet();
        pet1.setId(1L);
        Pet pet2 = new Pet();
        pet2.setId(pet1.getId());
        assertThat(pet1).isEqualTo(pet2);
        pet2.setId(2L);
        assertThat(pet1).isNotEqualTo(pet2);
        pet1.setId(null);
        assertThat(pet1).isNotEqualTo(pet2);
    }
}
