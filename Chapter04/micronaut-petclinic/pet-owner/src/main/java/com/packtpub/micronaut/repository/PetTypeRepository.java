package com.packtpub.micronaut.repository;

import com.packtpub.micronaut.domain.PetType;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Micronaut Data  repository for the PetType entity.
 */
@SuppressWarnings("unused")
@Repository
public abstract class PetTypeRepository implements JpaRepository<PetType, Long> {

    @PersistenceContext
    private final EntityManager entityManager;

    public PetTypeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public PetType mergeAndSave(PetType petType) {
        petType = entityManager.merge(petType);
        return save(petType);
    }

}
