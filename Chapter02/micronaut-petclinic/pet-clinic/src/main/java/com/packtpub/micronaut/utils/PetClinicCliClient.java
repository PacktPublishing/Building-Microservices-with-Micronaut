package com.packtpub.micronaut.utils;

import com.packtpub.micronaut.domain.Specialty;
import com.packtpub.micronaut.domain.Vet;
import com.packtpub.micronaut.service.SpecialtyService;
import com.packtpub.micronaut.service.VetService;
import io.micronaut.core.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class PetClinicCliClient {

    private final Logger log = LoggerFactory.getLogger(PetClinicCliClient.class);

    private final VetService vetService;
    private final SpecialtyService specialtyService;

    public PetClinicCliClient(VetService vetService, SpecialtyService specialtyService) {
        this.vetService = vetService;
        this.specialtyService = specialtyService;
    }

    public void performDatabaseOperations() {
        performFindAll();
        Vet vet = performSave();
        performFindOne(vet.getId());
        performDelete(vet);
        performFindAll();
    }

    protected void performFindAll() {
        log.info("------------------------------------------------------");
        log.info("Request to performFindAll");
        log.info("------------------------------------------------------");
        List<Vet> vets;
        try {
            vets = (List<Vet>) vetService.findAll();
            if (CollectionUtils.isNotEmpty(vets)) {
                vets.forEach(vet -> {
                    log.info("Vet: {} {}", vet.getFirstName(), vet.getLastName());
                });
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.toString());
        }
    }

    protected void performFindOne(Long id) {
        log.info("-----------------------------------------------------");
        log.info("Request to performFindOne for id: {}", id);
        log.info("-----------------------------------------------------");
        Optional<Vet> oVet;
        try {
            oVet = vetService.findOne(id);
            oVet.ifPresent(owner -> log.info("Vet: {}, {}", owner.getFirstName(), owner.getLastName()));
        } catch (Exception e) {
            log.error("Exception: {}", e.toString());
        }
    }

    protected Vet performSave() {
        Vet vet = initVet();
        log.info("-----------------------------------------------------");
        log.info("Request to performSave for vet: {}", vet);
        log.info("-----------------------------------------------------");
        Vet savedVet = null;
        try {
            savedVet = vetService.save(vet);
        } catch (Exception e) {
            log.error("Exception: {}", e.toString());
        }
        return savedVet;
    }

    protected void performDelete(Vet vet) {
        log.info("------------------------------------------------------");
        log.info("Request to performDelete for vet: {}", vet);
        log.info("-------------------------------------------------------");

        try {
            vetService.delete(vet.getId());
        } catch (Exception e) {
            log.error("Exception: {}", e.toString());
        }
    }

    private Vet initVet() {
        Vet vet = new Vet();
        vet.setFirstName("Foo");
        vet.setLastName("Bar");

        Specialty specialty = new Specialty();
        specialty.setName("Baz");

        vet.getSpecialties().add(specialty);

        return vet;
    }
}
