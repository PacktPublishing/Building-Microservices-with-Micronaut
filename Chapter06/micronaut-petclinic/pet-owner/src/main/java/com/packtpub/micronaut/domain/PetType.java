package com.packtpub.micronaut.domain;


import javax.persistence.*;
import java.io.Serializable;

/**
 * A PetType.
 */
@Entity
@Table(name = "types", schema = "petowner")
public class PetType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
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

    public PetType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PetType)) {
            return false;
        }
        return id != null && id.equals(((PetType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PetType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
