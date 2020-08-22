package com.packtpub.micronaut.service;

import com.packtpub.micronaut.domain.PetType;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.packtpub.micronaut.domain.PetType}.
 */
public interface PetTypeService {

    /**
     * Save a petType.
     *
     * @param petType the entity to save.
     * @return the persisted entity.
     */
    PetType save(PetType petType);

    /**
     * Get all the petTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PetType> findAll(Pageable pageable);


    /**
     * Get the "id" petType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PetType> findOne(Long id);

    /**
     * Delete the "id" petType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
