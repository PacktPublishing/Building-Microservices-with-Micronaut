package com.packtpub.micronaut;

import com.packtpub.micronaut.utils.PetClinicCliClient;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;

import javax.inject.Singleton;

@Singleton
public class Application {

    private final PetClinicCliClient petClinicCliClient;

    public Application(PetClinicCliClient petClinicCliClient) {
        this.petClinicCliClient = petClinicCliClient;
    }

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

    @EventListener
    void init(StartupEvent event) {
        petClinicCliClient.performDatabaseOperations();
    }
}
