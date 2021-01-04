package com.packtpub.micronaut.service.dto;

import io.micronaut.core.annotation.Introspected;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.packtpub.micronaut.domain.Pet} entity.
 */
@Introspected
public class PetDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate birthDate;

    private Set<VisitDTO> visits = new HashSet<>();

    private PetTypeDTO type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<VisitDTO> getVisits() {
        return visits;
    }

    public void setVisits(Set<VisitDTO> visits) {
        this.visits = visits;
    }

    public PetTypeDTO getType() {
        return type;
    }

    public void setType(PetTypeDTO type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetDTO petDTO = (PetDTO) o;
        return id.equals(petDTO.id) &&
                name.equals(petDTO.name) &&
                birthDate.equals(petDTO.birthDate) &&
                Objects.equals(visits, petDTO.visits) &&
                Objects.equals(type, petDTO.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthDate, visits, type);
    }

    @Override
    public String toString() {
        return "PetDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", visits=" + visits +
                ", type=" + type +
                '}';
    }
}
