package com.packtpub.micronaut.service.mapper;


import com.packtpub.micronaut.domain.Owner;
import com.packtpub.micronaut.service.dto.OwnerDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Owner} and its DTO {@link OwnerDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {PetMapper.class})
public interface OwnerMapper extends EntityMapper<OwnerDTO, Owner> {

    default Owner fromId(Long id) {
        if (id == null) {
            return null;
        }
        Owner owner = new Owner();
        owner.setId(id);
        return owner;
    }
}
