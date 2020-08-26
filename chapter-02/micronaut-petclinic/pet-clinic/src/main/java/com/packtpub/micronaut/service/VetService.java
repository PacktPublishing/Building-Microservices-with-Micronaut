package com.packtpub.micronaut.service;

import com.packtpub.micronaut.domain.Vet;

import java.util.Collection;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.packtpub.micronaut.domain.Vet}.
 */
public interface VetService {

    /**
     * Save a vet.
     *
     * @param vet the entity to save.
     * @return the persisted entity.
     */
    Vet save(Vet vet) throws Exception;

    /**
     * Get all the vets.
     *
     * @return the list of entities.
     */
    Collection<Vet> findAll() throws Exception;


    /**
     * Get the "id" vet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Vet> findOne(Long id) throws Exception;

    /**
     * Delete the "id" vet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id) throws Exception;
}
