package com.packtpub.micronaut.service.dto;

import io.micronaut.core.annotation.Introspected;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.packtpub.micronaut.domain.Owner} entity.
 */
@Introspected
public class OwnerDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String address;

    private String city;

    private String telephone;

    private Set<PetDTO> pets = new HashSet<>();

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Set<PetDTO> getPets() {
        return pets;
    }

    public void setPets(Set<PetDTO> pets) {
        this.pets = pets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwnerDTO ownerDTO = (OwnerDTO) o;
        return id.equals(ownerDTO.id) &&
                firstName.equals(ownerDTO.firstName) &&
                lastName.equals(ownerDTO.lastName) &&
                Objects.equals(address, ownerDTO.address) &&
                Objects.equals(city, ownerDTO.city) &&
                Objects.equals(telephone, ownerDTO.telephone) &&
                Objects.equals(pets, ownerDTO.pets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, address, city, telephone, pets);
    }

    @Override
    public String toString() {
        return "OwnerDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", telephone='" + telephone + '\'' +
                ", pets=" + pets +
                '}';
    }
}
