package com.packtpub.micronaut.service.mapper;


import com.packtpub.micronaut.domain.PetType;
import com.packtpub.micronaut.service.dto.PetTypeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link PetType} and its DTO {@link PetTypeDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {})
public interface PetTypeMapper extends EntityMapper<PetTypeDTO, PetType> {

    default PetType fromId(Long id) {
        if (id == null) {
            return null;
        }
        PetType petType = new PetType();
        petType.setId(id);
        return petType;
    }
}
