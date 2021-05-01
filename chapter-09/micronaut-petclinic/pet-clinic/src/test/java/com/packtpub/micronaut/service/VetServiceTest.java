package com.packtpub.micronaut.service;

import com.packtpub.micronaut.domain.Specialty;
import com.packtpub.micronaut.domain.Vet;
import com.packtpub.micronaut.repository.SpecialtyRepository;
import com.packtpub.micronaut.repository.VetRepository;
import com.packtpub.micronaut.repository.impl.SpecialtyRepositoryImpl;
import com.packtpub.micronaut.repository.impl.VetRepositoryImpl;
import com.packtpub.micronaut.service.dto.SpecialtyDTO;
import com.packtpub.micronaut.service.dto.VetDTO;
import com.packtpub.micronaut.service.mapper.SpecialtyMapper;
import com.packtpub.micronaut.service.mapper.VetMapper;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MicronautTest
class VetServiceTest {

    @Inject
    private VetRepository vetRepository;

    @Inject
    private SpecialtyRepository specialtyRepository;

    @Inject
    private VetMapper vetMapper;

    @Inject
    private SpecialtyMapper specialtyMapper;

    @Inject
    private VetService vetService;

    /** Mock beans */
    @MockBean(VetRepositoryImpl.class)
    VetRepository vetRepository() {
        return mock(VetRepository.class);
    }

    @MockBean(SpecialtyRepositoryImpl.class)
    SpecialtyRepository specialtyRepository() {
        return mock(SpecialtyRepository.class);
    }

    @Test
    public void saveVet() throws Exception {
        // Setup Specialty
        Long specialtyId = 100L;
        SpecialtyDTO specialtyDTO = createSpecialtyDTO(specialtyId);
        Specialty specialty = specialtyMapper.toEntity(specialtyDTO);

        // Setup VetDTO
        Long vetId = 200L;
        VetDTO vetDTO = createVetDTO(vetId);
        vetDTO.setSpecialties(Set.of(specialtyDTO));
        Vet vet = vetMapper.toEntity(vetDTO);

        // Stubbing
        when(vetRepository.save(any(Vet.class))).thenReturn(vetId);
        when(specialtyRepository.findByName(anyString())).thenReturn(specialty);
        doNothing().when(vetRepository).saveVetSpecialty(anyLong(), anyLong());
        when(vetRepository.findById(anyLong())).thenReturn(vet);

        // Execution
        VetDTO savedVetDTO = vetService.save(vetDTO);

        verify(vetRepository, times(1)).save(any(Vet.class));
        verify(specialtyRepository, times(1)).findByName(anyString());
        verify(vetRepository, times(1)).saveVetSpecialty(anyLong(), anyLong());
        verify(vetRepository, times(1)).findById(anyLong());

        assertThat(savedVetDTO).isNotNull();
        assertThat(savedVetDTO.getId()).isEqualTo(vetId);
        assertThat(savedVetDTO.getSpecialties()).isNotEmpty();
        assertThat(savedVetDTO.getSpecialties().size()).isEqualTo(1);
        assertThat(savedVetDTO.getSpecialties().stream().findFirst().orElse(null).getId()).isEqualTo(specialtyId);
    }

    @Test
    public void findAllVets() throws Exception {
        // Setup VetDTO
        Long vetId = 200L;
        VetDTO vetDTO = createVetDTO(vetId);
        Vet vet = vetMapper.toEntity(vetDTO);
        Collection<Vet> vets = List.of(vet);

        // Stubbing
        when(vetRepository.findAll()).thenReturn(vets);

        // Execution
        Collection<VetDTO> vetDTOs = vetService.findAll();

        verify(vetRepository, times(1)).findAll();

        assertThat(vetDTOs).isNotEmpty();
        assertThat(List.copyOf(vetDTOs)).isNotEmpty();
        assertThat(List.copyOf(vetDTOs).get(0).getId()).isEqualTo(vetId);
    }

    @Test
    public void findOneVet() throws Exception {
        // Setup VetDTO
        Long vetId = 200L;
        VetDTO vetDTO = createVetDTO(vetId);
        Vet vet = vetMapper.toEntity(vetDTO);

        // Stubbing
        when(vetRepository.findById(anyLong())).thenReturn(vet);

        // Execution
        Optional<VetDTO> oVet = vetService.findOne(vetId);

        verify(vetRepository, times(1)).findById(anyLong());

        assertThat(oVet.isPresent()).isTrue();
        assertThat(oVet.get().getId()).isEqualTo(vetId);
    }

    @Test
    public void deleteVet() throws Exception {
        // Setup VetDTO
        Long vetId = 200L;
        VetDTO vetDTO = createVetDTO(vetId);
        Vet vet = vetMapper.toEntity(vetDTO);

        // Stubbing
        when(vetRepository.findById(anyLong())).thenReturn(vet);
        doNothing().when(vetRepository).deleteById(anyLong());

        // Execution
        vetService.delete(vetId);

        verify(vetRepository, times(1)).findById(anyLong());
        verify(vetRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void updateVetAverageRating() throws Exception {
        // Setup VetDTO
        Long vetId = 200L;
        VetDTO vetDTO = createVetDTO(vetId);
        Vet vet = vetMapper.toEntity(vetDTO);

        // Stubbing
        when(vetRepository.findById(anyLong())).thenReturn(vet);
        doNothing().when(vetRepository).updateVetAverageRating(anyLong(), anyDouble(), anyLong());

        // Execution
        vetService.updateVetAverageRating(vetId, 3D);

        verify(vetRepository, times(1)).findById(anyLong());
        verify(vetRepository, times(1)).updateVetAverageRating(anyLong(), anyDouble(), anyLong());
    }

    private VetDTO createVetDTO(Long id) {
        VetDTO vetDTO = new VetDTO();
        vetDTO.setId(id);
        vetDTO.setFirstName("Foo");
        vetDTO.setLastName("Bar");
        vetDTO.setAverageRating(new Random().nextDouble());
        vetDTO.setRatingCount(new Random().nextLong());
        return vetDTO;
    }

    private SpecialtyDTO createSpecialtyDTO(Long id) {
        SpecialtyDTO specialtyDTO = new SpecialtyDTO();
        specialtyDTO.setId(id);
        specialtyDTO.setName("Baz");
        return specialtyDTO;
    }
}
