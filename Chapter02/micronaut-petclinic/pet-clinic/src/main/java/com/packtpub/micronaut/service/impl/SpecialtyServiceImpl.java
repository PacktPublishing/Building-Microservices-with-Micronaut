package com.packtpub.micronaut.service.impl;

import com.packtpub.micronaut.domain.Specialty;
import com.packtpub.micronaut.repository.SpecialtyRepository;
import com.packtpub.micronaut.service.SpecialtyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Specialty}.
 */
@Singleton
public class SpecialtyServiceImpl implements SpecialtyService {

    private final Logger log = LoggerFactory.getLogger(SpecialtyServiceImpl.class);

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    /**
     * Save a specialty.
     *
     * @param specialty the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Specialty save(Specialty specialty) throws Exception {
        log.debug("Request to save Specialty : {}", specialty);
        Long specialtyId = specialtyRepository.save(specialty);
        return specialtyRepository.findById(specialtyId);
    }

    /**
     * Get all the specialties.
     *
     * @return the list of entities.
     */
    @Override
    public Collection<Specialty> findAll() throws Exception {
        log.debug("Request to get all Specialties");
        return specialtyRepository.findAll();
    }


    /**
     * Get one specialty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Specialty> findOne(Long id) throws Exception {
        log.debug("Request to get Specialty : {}", id);
        return Optional.ofNullable(specialtyRepository.findById(id));
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
