package com.packtpub.micronaut.service.dto;

import io.micronaut.core.annotation.Introspected;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.packtpub.micronaut.domain.Vet} entity.
 */
@Introspected
public class VetDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private Set<SpecialtyDTO> specialties = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<SpecialtyDTO> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(Set<SpecialtyDTO> specialties) {
        this.specialties = specialties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VetDTO vetDTO = (VetDTO) o;
        return id.equals(vetDTO.id) &&
                Objects.equals(firstName, vetDTO.firstName) &&
                Objects.equals(lastName, vetDTO.lastName) &&
                Objects.equals(specialties, vetDTO.specialties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, specialties);
    }

    @Override
    public String toString() {
        return "VetDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialties=" + specialties +
                '}';
    }
}
