package com.packtpub.micronaut.service.mapper;


import com.packtpub.micronaut.domain.Vet;
import com.packtpub.micronaut.service.dto.VetDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Vet} and its DTO {@link VetDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {SpecialtyMapper.class})
public interface VetMapper extends EntityMapper<VetDTO, Vet> {

    default Vet fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vet vet = new Vet();
        vet.setId(id);
        return vet;
    }
}
