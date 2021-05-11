package com.packtpub.micronaut.service.impl;

import com.packtpub.micronaut.domain.Specialty;
import com.packtpub.micronaut.repository.SpecialtyRepository;
import com.packtpub.micronaut.service.SpecialtyService;
import com.packtpub.micronaut.service.dto.SpecialtyDTO;
import com.packtpub.micronaut.service.mapper.SpecialtyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Specialty}.
 */
@Singleton
public class SpecialtyServiceImpl implements SpecialtyService {

    private final Logger log = LoggerFactory.getLogger(SpecialtyServiceImpl.class);

    private final SpecialtyRepository specialtyRepository;

    private final SpecialtyMapper specialtyMapper;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper) {
        this.specialtyRepository = specialtyRepository;
        this.specialtyMapper = specialtyMapper;
    }

    /**
     * Save a specialtyDTO.
     *
     * @param specialtyDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SpecialtyDTO save(SpecialtyDTO specialtyDTO) throws Exception {
        log.debug("Request to save Specialty : {}", specialtyDTO);
        Specialty specialty = specialtyMapper.toEntity(specialtyDTO);
        Long specialtyId = specialtyRepository.save(specialty);
        return specialtyMapper.toDto(specialtyRepository.findById(specialtyId));
    }

    /**
     * Get all the specialties.
     *
     * @return the list of entities.
     */
    @Override
    public Collection<SpecialtyDTO> findAll() throws Exception {
        log.debug("Request to get all Specialties");
        return specialtyRepository.findAll()
                .stream()
                .map(specialtyMapper::toDto)
                .collect(Collectors.toList());
    }


    /**
     * Get one specialty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<SpecialtyDTO> findOne(Long id) throws Exception {
        log.debug("Request to get Specialty : {}", id);
        return Optional.ofNullable(specialtyRepository.findById(id)).map(specialtyMapper::toDto);
    }

    /**
     * Delete the specialty by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) throws Exception {
        log.debug("Request to delete Specialty : {}", id);
        specialtyRepository.deleteById(id);
    }
}
