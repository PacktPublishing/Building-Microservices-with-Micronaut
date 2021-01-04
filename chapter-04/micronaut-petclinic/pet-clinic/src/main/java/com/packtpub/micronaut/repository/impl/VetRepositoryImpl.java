package com.packtpub.micronaut.repository.impl;

import com.packtpub.micronaut.domain.Vet;
import com.packtpub.micronaut.repository.VetRepository;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.inject.Singleton;
import java.util.Collection;

/**
 * Mapper implementation for {@link Vet}.
 */
@Singleton
public class VetRepositoryImpl implements VetRepository {

    private final SqlSessionFactory sqlSessionFactory;

    public VetRepositoryImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    private VetRepository getVetRepository(SqlSession sqlSession) {
        return sqlSession.getMapper(VetRepository.class);
    }

    @Override
    public Collection<Vet> findAll() throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return getVetRepository(sqlSession).findAll();
        }
    }

    @Override
    public Vet findById(Long id) throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return getVetRepository(sqlSession).findById(id);
        }
    }

    @Override
    public Long save(Vet vet) throws Exception {
        Long vetId;
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            vetId = getVetRepository(sqlSession).save(vet);
            sqlSession.commit();
        }
        return vetId;
    }

    @Override
    public void saveVetSpecialty(Long vetId, Long specialtyId) throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            getVetRepository(sqlSession).saveVetSpecialty(vetId, specialtyId);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteById(Long id) throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            getVetRepository(sqlSession).deleteById(id);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteVetSpecialtyById(Long vetId) throws Exception {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            getVetRepository(sqlSession).deleteVetSpecialtyById(vetId);
            sqlSession.commit();
        }
    }
}
