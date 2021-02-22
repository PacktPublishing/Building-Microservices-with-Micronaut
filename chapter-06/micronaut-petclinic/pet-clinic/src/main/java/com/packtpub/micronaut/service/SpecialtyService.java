package com.packtpub.micronaut.service;

import com.packtpub.micronaut.domain.Specialty;
import com.packtpub.micronaut.service.dto.SpecialtyDTO;

import java.util.Collection;
import java.util.Optional;

/**
 * Service Interface for managing {@link Specialty}.
 */
public interface SpecialtyService {

    /**
     * Save a speciality.
     *
     * @param specialityDTO the entity to save.
     * @return the persisted entity.
     */
    SpecialtyDTO save(SpecialtyDTO specialityDTO) throws Exception;

    /**
     * Get all the specialties.
     *
     * @return the list of entities.
     */
    Collection<SpecialtyDTO> findAll() throws Exception;


    /**
     * Get the "id" specialty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpecialtyDTO> findOne(Long id) throws Exception;

    /**
     * Delete the "id" specialty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id) throws Exception;
}
