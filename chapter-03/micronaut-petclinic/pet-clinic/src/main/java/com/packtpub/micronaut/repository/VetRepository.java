package com.packtpub.micronaut.repository;

import com.packtpub.micronaut.domain.Vet;
import org.apache.ibatis.annotations.*;

import java.util.Collection;

/**
 * Mybatis mapper for {@link Vet}.
 */
public interface VetRepository {

    @Select("SELECT * FROM petclinic.vets")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
    })
    Collection<Vet> findAll() throws Exception;

    @Select("SELECT * FROM petclinic.vets WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
    })
    Vet findById(@Param("id") Long id) throws Exception;

    @Select("INSERT INTO petclinic.vets (first_name, last_name) VALUES(#{firstName}, #{lastName}) " +
            "RETURNING id")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    Long save(Vet vet) throws Exception;

    @Insert("INSERT INTO petclinic.vet_specialties (vet_id, specialty_id) VALUES(#{vetId}, #{specialtyId})")
    void saveVetSpecialty(@Param("vetId") Long vetId, @Param("specialtyId") Long specialtyId) throws Exception;

    @Delete("DELETE FROM petclinic.vets WHERE id = #{id}")
    void deleteById(@Param("id") Long id) throws Exception;

    @Delete("DELETE FROM petclinic.vet_specialties WHERE vet_id = #{vetId}")
    void deleteVetSpecialtyById(@Param("vetId") Long vetId) throws Exception;
}
