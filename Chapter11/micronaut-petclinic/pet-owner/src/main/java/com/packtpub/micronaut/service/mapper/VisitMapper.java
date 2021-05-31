package com.packtpub.micronaut.service.mapper;


import com.packtpub.micronaut.domain.Visit;
import com.packtpub.micronaut.service.dto.VisitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Visit} and its DTO {@link VisitDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {})
public interface VisitMapper extends EntityMapper<VisitDTO, Visit> {

    @Mapping(source = "pet.id", target = "petId")
    VisitDTO toDto(Visit visit);

    @Mapping(source = "petId", target = "pet.id")
    Visit toEntity(VisitDTO visitDTO);

    default Visit fromId(Long id) {
        if (id == null) {
            return null;
        }
        Visit visit = new Visit();
        visit.setId(id);
        return visit;
    }
}
