package com.packtpub.micronaut.repository;

import com.packtpub.micronaut.domain.Pet;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Micronaut Data  repository for the Pet entity.
 */
@SuppressWarnings("unused")
@Repository
public abstract class PetRepository implements JpaRepository<Pet, Long> {

    @PersistenceContext
    private final EntityManager entityManager;

    public PetRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Pet mergeAndSave(Pet pet) {
        pet = entityManager.merge(pet);
        return save(pet);
    }

}
