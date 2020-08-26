package com.packtpub.micronaut.service;

import com.packtpub.micronaut.domain.Specialty;

import java.util.Collection;
import java.util.Optional;

/**
 * Service Interface for managing {@link Specialty}.
 */
public interface SpecialtyService {

    /**
     * Save a specialty.
     *
     * @param specialty the entity to save.
     * @return the persisted entity.
     */
    Specialty save(Specialty specialty) throws Exception;

    /**
     * Get all the specialties.
     *
     * @return the list of entities.
     */
    Collection<Specialty> findAll() throws Exception;


    /**
     * Get the "id" specialty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Specialty> findOne(Long id) throws Exception;

    /**
     * Delete the "id" specialty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id) throws Exception;
}
