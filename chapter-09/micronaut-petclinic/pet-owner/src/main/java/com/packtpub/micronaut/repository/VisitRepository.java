package com.packtpub.micronaut.repository;

import com.packtpub.micronaut.domain.Visit;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Micronaut Data  repository for the Visit entity.
 */
@SuppressWarnings("unused")
@Repository
public abstract class VisitRepository implements JpaRepository<Visit, Long> {

    @PersistenceContext
    private final EntityManager entityManager;

    public VisitRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Visit mergeAndSave(Visit visit) {
        visit = entityManager.merge(visit);
        return save(visit);
    }

}
