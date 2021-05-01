package com.packtpub.alexa;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        String speechText = "Welcome to Pet Clinic, You can say find near by Pet Clinics";
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("PetClinic", speechText)
                .withReprompt(speechText)
                .build();
    }
}