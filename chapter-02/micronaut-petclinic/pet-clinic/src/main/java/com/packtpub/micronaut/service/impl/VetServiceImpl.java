package com.packtpub.micronaut.service.impl;

import com.packtpub.micronaut.domain.Specialty;
import com.packtpub.micronaut.domain.Vet;
import com.packtpub.micronaut.repository.SpecialtyRepository;
import com.packtpub.micronaut.repository.VetRepository;
import com.packtpub.micronaut.service.VetService;
import io.micronaut.core.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Vet}.
 */
@Singleton
public class VetServiceImpl implements VetService {

    private final Logger log = LoggerFactory.getLogger(VetServiceImpl.class);

    private final VetRepository vetRepository;

    private final SpecialtyRepository specialtyRepository;

    public VetServiceImpl(VetRepository vetRepository, SpecialtyRepository specialtyRepository) {
        this.vetRepository = vetRepository;
        this.specialtyRepository = specialtyRepository;
    }

    /**
     * Save a vet.
     *
     * @param vet the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Vet save(Vet vet) {
        log.debug("Request to save Vet : {}", vet);
        Vet savedVet = null;
        try {
            Long vetId = vetRepository.save(vet);
            saveSpecialties(vetId, vet.getSpecialties());
            savedVet = vetRepository.findById(vetId);
        } catch (Exception e) {
            log.error("Exception: {}", e.toString());
        }
        return savedVet;
    }

    /**
     * Get all the vets.
     *
     * @return the list of entities.
     */
    @Override
    public Collection<Vet> findAll() throws Exception {
        log.debug("Request to get all Vets");
        Collection<Vet> vets = vetRepository.findAll();
        vets.forEach(vet -> {
            try {
                Set<Specialty> specialties = specialtyRepository.findByVetId(vet.getId());
                if(CollectionUtils.isNotEmpty(specialties)) {
                    vet.setSpecialties(specialties);
                }
            } catch (Exception e) {
                log.error("Exception: {}", e.toString());
            }
        });
        return vets;
    }


    /**
     * Get one vet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Vet> findOne(Long id) throws Exception {
        log.debug("Request to get Vet : {}", id);
        Vet vet = vetRepository.findById(id);
        if (vet != null) {
            try {
                Set<Specialty> specialties = specialtyRepository.findByVetId(vet.getId());
                if(CollectionUtils.isNotEmpty(specialties)) {
                    vet.setSpecialties(specialties);
                }
            } catch (Exception e) {
                log.error("Exception: {}", e.toString());
            }
        }
        return Optional.ofNullable(vet);
    }

    /**
     * Delete the vet by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) throws Exception {
        log.debug("Request to delete Vet : {}", id);
        Optional<Vet> oVet = findOne(id);
        if (oVet.isPresent()) {
            /** Delete any vet_specialties record */
            if (CollectionUtils.isNotEmpty(oVet.get().getSpecialties())) {
                vetRepository.deleteVetSpecialtyById(oVet.get().getId());
            }
            /** Delete vet */
            vetRepository.deleteById(id);
        }
    }

    private void saveSpecialties(Long vetId, Set<Specialty> specialties) {
        log.debug("Request to save Specialties : {}", specialties);
        if (CollectionUtils.isNotEmpty(specialties)) {
            specialties.forEach(specialty -> {
                try {
                    Long specialtyId;

                    Specialty dbSpecialty = specialtyRepository.findByName(specialty.getName().toUpperCase().trim());

                    if (dbSpecialty != null) {
                        specialtyId = dbSpecialty.getId();
                    } else {
                        specialtyId = specialtyRepository.save(specialty);
                    }

                    vetRepository.saveVetSpecialty(vetId, specialtyId);
                } catch (Exception e) {
                    log.error("Exception: {}", e.toString());
                }
            });
        }
    }
}
