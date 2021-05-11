package com.packtpub.micronaut.service.mapper;


import com.packtpub.micronaut.domain.Pet;
import com.packtpub.micronaut.service.dto.PetDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Pet} and its DTO {@link PetDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {PetTypeMapper.class, VisitMapper.class})
public interface PetMapper extends EntityMapper<PetDTO, Pet> {

    default Pet fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pet pet = new Pet();
        pet.setId(id);
        return pet;
    }
}
