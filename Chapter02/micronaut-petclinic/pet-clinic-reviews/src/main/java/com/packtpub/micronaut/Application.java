package com.packtpub.micronaut;

import com.packtpub.micronaut.utils.PetClinicReviewCliClient;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;

import javax.inject.Singleton;

@Singleton
public class Application {

    private final PetClinicReviewCliClient petClinicReviewCliClient;

    public Application(PetClinicReviewCliClient petClinicReviewCliClient) {
        this.petClinicReviewCliClient = petClinicReviewCliClient;
    }

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

    @EventListener
    void init(StartupEvent event) {
        petClinicReviewCliClient.performDatabaseOperations();
    }
}
