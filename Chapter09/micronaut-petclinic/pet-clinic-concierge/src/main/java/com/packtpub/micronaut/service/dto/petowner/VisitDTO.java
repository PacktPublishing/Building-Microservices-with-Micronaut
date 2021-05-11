package com.packtpub.micronaut.service.dto.petowner;

import io.micronaut.core.annotation.Introspected;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the Visit entity.
 */
@Introspected
public class VisitDTO implements Serializable {

    private Long id;

    private LocalDate visitDate;

    private String description;

    private Long petId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitDTO visitDTO = (VisitDTO) o;
        return id.equals(visitDTO.id) &&
                visitDate.equals(visitDTO.visitDate) &&
                Objects.equals(description, visitDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, visitDate, description);
    }

    @Override
    public String toString() {
        return "VisitDTO{" +
                "id=" + id +
                ", visitDate=" + visitDate +
                ", description='" + description + '\'' +
                ", petId=" + petId +
                '}';
    }
}
