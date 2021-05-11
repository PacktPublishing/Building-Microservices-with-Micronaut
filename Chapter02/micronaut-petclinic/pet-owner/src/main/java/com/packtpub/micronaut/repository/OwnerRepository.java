package com.packtpub.micronaut.repository;

import com.packtpub.micronaut.domain.Owner;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Micronaut Data  repository for the Owner entity.
 */
@SuppressWarnings("unused")
@Repository
public abstract class OwnerRepository implements JpaRepository<Owner, Long> {

    @PersistenceContext
    private final EntityManager entityManager;

    public OwnerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Owner mergeAndSave(Owner owner) {
        owner = entityManager.merge(owner);
        return save(owner);
    }

}
