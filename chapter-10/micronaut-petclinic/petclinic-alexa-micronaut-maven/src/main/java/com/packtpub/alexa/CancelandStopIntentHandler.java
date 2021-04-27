package com.packtpub.alexa;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import javax.inject.Singleton;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Singleton
public class CancelandStopIntentHandler implements RequestHandler {

    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.StopIntent").or(intentName("AMAZON.CancelIntent")));
    }

    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Thank you for using Pet Clinic";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("PetClinic", speechText)
                .withShouldEndSession(true)
                .build();
    }

}
