package com.packtpub.micronaut.service.mapper;


import com.packtpub.micronaut.domain.Specialty;
import com.packtpub.micronaut.service.dto.SpecialtyDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Specialty} and its DTO {@link SpecialtyDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {})
public interface SpecialtyMapper extends EntityMapper<SpecialtyDTO, Specialty> {

    default Specialty fromId(Long id) {
        if (id == null) {
            return null;
        }
        Specialty specialty = new Specialty();
        specialty.setId(id);
        return specialty;
    }
}
