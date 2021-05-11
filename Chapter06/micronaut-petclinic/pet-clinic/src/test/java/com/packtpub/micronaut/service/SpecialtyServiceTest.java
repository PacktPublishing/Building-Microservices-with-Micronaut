package com.packtpub.micronaut.service;

import com.packtpub.micronaut.domain.Specialty;
import com.packtpub.micronaut.repository.SpecialtyRepository;
import com.packtpub.micronaut.repository.impl.SpecialtyRepositoryImpl;
import com.packtpub.micronaut.service.dto.SpecialtyDTO;
import com.packtpub.micronaut.service.mapper.SpecialtyMapper;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@MicronautTest
class SpecialtyServiceTest {

    @Inject
    private SpecialtyRepository specialtyRepository;

    @Inject
    private SpecialtyMapper specialtyMapper;

    @Inject
    private SpecialtyService specialtyService;

    @MockBean(SpecialtyRepositoryImpl.class)
    SpecialtyRepository specialtyRepository() {
        return spy(SpecialtyRepository.class);
    }

    @Test
    public void saveSpecialty() throws Exception {
        // Setup Specialty
        Long specialtyId = 100L;
        SpecialtyDTO specialtyDTO = createSpecialtyDTO(specialtyId);
        Specialty specialty = specialtyMapper.toEntity(specialtyDTO);

        // Stubbing
        doReturn(100L).when(specialtyRepository).save(any(Specialty.class));
        doReturn(specialty).when(specialtyRepository).findById(anyLong());

        // Execution
        SpecialtyDTO savedSpecialtyDTO = specialtyService.save(specialtyDTO);

        verify(specialtyRepository, times(1)).save(any(Specialty.class));
        verify(specialtyRepository, times(1)).findById(anyLong());

        assertThat(savedSpecialtyDTO).isNotNull();
        assertThat(savedSpecialtyDTO.getId()).isEqualTo(specialtyId);
    }

    @Test
    public void findAllSpecialties() throws Exception {
        // Setup Specialty
        Long specialtyId = 100L;
        SpecialtyDTO specialtyDTO = createSpecialtyDTO(specialtyId);
        Specialty specialty = specialtyMapper.toEntity(specialtyDTO);

        // Stubbing
        when(specialtyRepository.findAll()).thenReturn(List.of(specialty));

        // Execution
        Collection<SpecialtyDTO> specialtyDTOs = specialtyService.findAll();

        verify(specialtyRepository, times(1)).findAll();

        assertThat(specialtyDTOs).isNotEmpty();
        assertThat(List.copyOf(specialtyDTOs)).isNotEmpty();
        assertThat(List.copyOf(specialtyDTOs).get(0).getId()).isEqualTo(specialtyId);
    }

    @Test
    public void findOneSpecialty() throws Exception {
        // Setup Specialty
        Long specialtyId = 100L;
        SpecialtyDTO specialtyDTO = createSpecialtyDTO(specialtyId);
        Specialty specialty = specialtyMapper.toEntity(specialtyDTO);

        // Stubbing
        when(specialtyRepository.findById(anyLong())).thenReturn(specialty);

        // Execution
        Optional<SpecialtyDTO> oSpecialty = specialtyService.findOne(specialtyId);

        verify(specialtyRepository, times(1)).findById(anyLong());

        assertThat(oSpecialty.isPresent()).isTrue();
        assertThat(oSpecialty.get().getId()).isEqualTo(specialtyId);
    }

    @Test
    public void deleteSpecialty() throws Exception {
        Long vetId = 200L;

        // Stubbing
        doNothing().when(specialtyRepository).deleteById(anyLong());

        // Execution
        specialtyService.delete(vetId);

        verify(specialtyRepository, times(1)).deleteById(anyLong());
    }

    private SpecialtyDTO createSpecialtyDTO(Long id) {
        SpecialtyDTO specialtyDTO = new SpecialtyDTO();
        specialtyDTO.setId(id);
        specialtyDTO.setName("Baz");
        return specialtyDTO;
    }

}
