package com.packtpub.alexa;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import javax.inject.Singleton;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Singleton
public class PetClinicWelcomeIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("PetClinicWelcomeIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        String speechText = "The nearest pet clinics are";
        speechText =  speechText + " Animal Clinic, 75 Matheson Blvd East, New York, NY";
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("PetClinic", speechText)
                .withReprompt(speechText)
                .build();
    }
}
