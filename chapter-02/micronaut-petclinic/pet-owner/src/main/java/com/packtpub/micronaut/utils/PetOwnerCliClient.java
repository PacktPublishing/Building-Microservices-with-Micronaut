package com.packtpub.micronaut.utils;

import com.packtpub.micronaut.domain.Owner;
import com.packtpub.micronaut.domain.Pet;
import com.packtpub.micronaut.domain.Visit;
import com.packtpub.micronaut.service.OwnerService;
import com.packtpub.micronaut.service.PetService;
import com.packtpub.micronaut.service.PetTypeService;
import com.packtpub.micronaut.service.VisitService;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Singleton
public class PetOwnerCliClient {

    private final Logger log = LoggerFactory.getLogger(PetOwnerCliClient.class);

    private final OwnerService ownerService;
    private final PetService petService;
    private final VisitService visitService;
    private final PetTypeService petTypeService;

    public PetOwnerCliClient(OwnerService ownerService, PetService petService, VisitService visitService, PetTypeService petTypeService) {
        this.ownerService = ownerService;
        this.petService = petService;
        this.visitService = visitService;
        this.petTypeService = petTypeService;
    }

    public void performDatabaseOperations() {
        performFindAll();
        Owner owner = performSave();
        performFindOne(owner.getId());
        performDelete(owner);
        performFindAll();
    }

    protected void performFindAll() {
        log.info("------------------------------------------------------");
        log.info("Request to performFindAll");
        log.info("------------------------------------------------------");
        Page<Owner> pOwners = ownerService.findAll(Pageable.unpaged());

        if (CollectionUtils.isNotEmpty(pOwners.getContent())) {
            List<Owner> owners = pOwners.getContent();
            owners.forEach(owner -> {
                log.info("Owner: {}, {}", owner.getFirstName(), owner.getLastName());
            });
        }
    }

    protected void performFindOne(Long id) {
        log.info("-----------------------------------------------------");
        log.info("Request to performFindOne for id: {}", id);
        log.info("-----------------------------------------------------");
        Optional<Owner> oOwner = ownerService.findOne(id);
        oOwner.ifPresent(owner -> log.info("Owner: {}, {}", owner.getFirstName(), owner.getLastName()));
    }

    protected Owner performSave() {
        Owner owner = initOwner();
        log.info("-----------------------------------------------------");
        log.info("Request to performSave for owner: {}", owner);
        log.info("-----------------------------------------------------");
        return ownerService.save(owner);
    }

    protected void performDelete(Owner owner) {
        log.info("------------------------------------------------------");
        log.info("Request to performDelete for owner: {}", owner);
        log.info("-------------------------------------------------------");

        /** delete owner pets and their visits */
        Set<Pet> pets = owner.getPets();

        if (CollectionUtils.isNotEmpty(pets)) {
            for (Pet pet : pets) {
                Set<Visit> visits = pet.getVisits();
                if (CollectionUtils.isNotEmpty(visits)) {
                    for (Visit visit : visits) {
                        visitService.delete(visit.getId());
                    }
                }
                petService.delete(pet.getId());
            }
        }

        ownerService.delete(owner.getId());
    }

    private Owner initOwner() {
        Owner owner = new Owner();

        owner.setFirstName("Foo");
        owner.setLastName("Bar");
        owner.setCity("Toronto");
        owner.setAddress("404 Adelaide St W");
        owner.setTelephone("647000999");

        Pet pet = new Pet();
        pet.setType(petTypeService.findAll(Pageable.unpaged()).getContent().get(1));
        pet.setName("Baz");
        pet.setBirthDate(LocalDate.of(2010, 12, 12));
        pet.setOwner(owner);

        Visit visit = new Visit();
        visit.setVisitDate(LocalDate.now());
        visit.setDescription("Breathing issues");
        visit.setPet(pet);

        return owner;
    }
}
