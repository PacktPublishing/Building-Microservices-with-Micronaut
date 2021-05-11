package com.packtpub.micronaut.service.mapper;


import com.packtpub.micronaut.domain.Pet;
import com.packtpub.micronaut.service.dto.PetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Pet} and its DTO {@link PetDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {PetTypeMapper.class, VisitMapper.class})
public interface PetMapper extends EntityMapper<PetDTO, Pet> {

    @Mapping(source = "owner.id", target = "ownerId")
    PetDTO toDto(Pet pet);

    @Mapping(source = "ownerId", target = "owner.id")
    Pet toEntity(PetDTO petDTO);

    default Pet fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pet pet = new Pet();
        pet.setId(id);
        return pet;
    }
}
