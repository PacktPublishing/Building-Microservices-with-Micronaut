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
        @Result(property = "averageRating", column = "avg_rating"),
        @Result(property = "ratingCount", column = "rating_count"),
    })
    Collection<Vet> findAll() throws Exception;

    @Select("SELECT * FROM petclinic.vets WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "firstName", column = "first_name"),
        @Result(property = "lastName", column = "last_name"),
        @Result(property = "averageRating", column = "avg_rating"),
        @Result(property = "ratingCount", column = "rating_count"),
    })
    Vet findById(@Param("id") Long id) throws Exception;

    @Select(
        {
            "INSERT INTO petclinic.vets(id, first_name, last_name)\n" +
                "VALUES (COALESCE(#{id}, (select nextval('petclinic.vets_id_seq'))), #{firstName}, #{lastName})\n" +
                "ON CONFLICT (id)\n" +
                "DO UPDATE SET (first_name, last_name) = (#{firstName}, #{lastName})\n" +
                "WHERE petclinic.vets.id = #{id}\n" +
                "RETURNING id"
        }
    )
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    Long save(Vet vet) throws Exception;

    @Select(
        {
            "INSERT INTO petclinic.vet_specialties(vet_id, specialty_id)\n" +
                "VALUES (#{vetId}, #{specialtyId})\n" +
                "ON CONFLICT (vet_id, specialty_id)\n" +
                "DO NOTHING\n"
        }
    )
    void saveVetSpecialty(@Param("vetId") Long vetId, @Param("specialtyId") Long specialtyId) throws Exception;

    @Delete("DELETE FROM petclinic.vets WHERE id = #{id}")
    void deleteById(@Param("id") Long id) throws Exception;

    @Delete("DELETE FROM petclinic.vet_specialties WHERE vet_id = #{vetId}")
    void deleteVetSpecialtyById(@Param("vetId") Long vetId) throws Exception;

    @Update("UPDATE petclinic.vets SET avg_rating = #{averageRating}, rating_count = #{ratingCount} WHERE id = #{id}")
    void updateVetAverageRating(
        @Param("id") Long id,
        @Param("averageRating") Double averageRating,
        @Param("ratingCount") Long ratingCount
    ) throws Exception;
}
