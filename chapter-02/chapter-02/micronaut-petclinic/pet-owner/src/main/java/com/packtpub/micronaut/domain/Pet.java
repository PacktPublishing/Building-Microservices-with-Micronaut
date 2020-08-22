package com.packtpub.micronaut.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Pet.
 */
@Entity
@Table(name = "pets", schema = "petowner")
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "pet", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Visit> visits = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PetType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Pet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Pet birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Visit> getVisits() {
        return visits;
    }

    public Pet visits(Set<Visit> visits) {
        this.visits = visits;
        return this;
    }

    public Pet addVisit(Visit visit) {
        this.visits.add(visit);
        visit.setPet(this);
        return this;
    }

    public Pet removeVisit(Visit visit) {
        this.visits.remove(visit);
        visit.setPet(null);
        return this;
    }

    public void setVisits(Set<Visit> visits) {
        this.visits = visits;
    }

    public PetType getType() {
        return type;
    }

    public Pet type(PetType petType) {
        this.type = petType;
        return this;
    }

    public void setType(PetType petType) {
        this.type = petType;
    }

    public Owner getOwner() {
        return owner;
    }

    public Pet owner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pet)) {
            return false;
        }
        return id != null && id.equals(((Pet) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Pet{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }
}
