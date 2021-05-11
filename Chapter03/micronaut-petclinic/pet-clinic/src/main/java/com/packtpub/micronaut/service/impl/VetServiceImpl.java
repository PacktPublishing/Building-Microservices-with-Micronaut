package com.packtpub.micronaut.service.impl;

import com.packtpub.micronaut.domain.Specialty;
import com.packtpub.micronaut.domain.Vet;
import com.packtpub.micronaut.repository.SpecialtyRepository;
import com.packtpub.micronaut.repository.VetRepository;
import com.packtpub.micronaut.service.VetService;
import com.packtpub.micronaut.service.dto.SpecialtyDTO;
import com.packtpub.micronaut.service.dto.VetDTO;
import com.packtpub.micronaut.service.mapper.SpecialtyMapper;
import com.packtpub.micronaut.service.mapper.VetMapper;
import io.micronaut.core.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
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

    private final VetMapper vetMapper;

    private final SpecialtyMapper specialtyMapper;

    public VetServiceImpl(VetRepository vetRepository, SpecialtyRepository specialtyRepository, VetMapper vetMapper, SpecialtyMapper specialtyMapper) {
        this.vetRepository = vetRepository;
        this.specialtyRepository = specialtyRepository;
        this.vetMapper = vetMapper;
        this.specialtyMapper = specialtyMapper;
    }

    /**
     * Save a vetDTO.
     *
     * @param vetDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public VetDTO save(VetDTO vetDTO) {
        log.debug("Request to save Vet : {}", vetDTO);
        Vet savedVet = null;
        try {
            Long vetId = vetRepository.save(vetMapper.toEntity(vetDTO));
            saveSpecialties(vetId, vetDTO.getSpecialties());
            savedVet = vetRepository.findById(vetId);
        } catch (Exception e) {
            log.error("Exception: {}", e.toString());
        }
        return vetMapper.toDto(savedVet);
    }

    /**
     * Get all the vets.
     *
     * @return the list of entities.
     */
    @Override
    public Collection<VetDTO> findAll() throws Exception {
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
        return vetMapper.toDto((List<Vet>) vets);
    }


    /**
     * Get one vet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<VetDTO> findOne(Long id) throws Exception {
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
        return Optional.ofNullable(vetMapper.toDto(vet));
    }

    /**
     * Delete the vet by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) throws Exception {
        log.debug("Request to delete Vet : {}", id);
        Optional<VetDTO> oVetDTO = findOne(id);
        if (oVetDTO.isPresent()) {
            /** Delete any vet_specialties record */
            if (CollectionUtils.isNotEmpty(oVetDTO.get().getSpecialties())) {
                vetRepository.deleteVetSpecialtyById(oVetDTO.get().getId());
            }
            /** Delete vet */
            vetRepository.deleteById(id);
        }
    }

    private void saveSpecialties(Long vetId, Set<SpecialtyDTO> specialties) {
        log.debug("Request to save Specialties : {}", specialties);
        if (CollectionUtils.isNotEmpty(specialties)) {
            specialties.forEach(specialty -> {
                try {
                    Long specialtyId;

                    Specialty dbSpecialty = specialtyRepository.findByName(specialty.getName().toUpperCase().trim());

                    if (dbSpecialty != null) {
                        specialtyId = dbSpecialty.getId();
                    } else {
                        specialtyId = specialtyRepository.save(specialtyMapper.toEntity(specialty));
                    }

                    vetRepository.saveVetSpecialty(vetId, specialtyId);
                } catch (Exception e) {
                    log.error("Exception: {}", e.toString());
                }
            });
        }
    }
}
