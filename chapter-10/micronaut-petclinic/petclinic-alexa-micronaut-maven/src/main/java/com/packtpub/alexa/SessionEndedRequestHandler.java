package com.packtpub.alexa;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;

import javax.inject.Singleton;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

@Singleton
public class SessionEndedRequestHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(requestType(SessionEndedRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return handlerInput.getResponseBuilder().build();
    }
}

