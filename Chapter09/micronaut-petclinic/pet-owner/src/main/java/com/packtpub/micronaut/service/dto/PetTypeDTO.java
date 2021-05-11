package com.packtpub.micronaut.service.dto;

import io.micronaut.core.annotation.Introspected;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.packtpub.micronaut.domain.PetType} entity.
 */
@Introspected
public class PetTypeDTO implements Serializable {

    private Long id;

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetTypeDTO that = (PetTypeDTO) o;
        return id.equals(that.id) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "PetTypeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
