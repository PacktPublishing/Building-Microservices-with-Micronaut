package com.packtpub.alexa.test;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.packtpub.alexa.CancelandStopIntentHandler;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class CancelandStopIntentHandlerTest {

    @Inject
    private CancelandStopIntentHandler handler;

    @Test
    void testCancelIntentHandler() {
        HandlerInput input = HandlerInput.builder()
                .withRequestEnvelope(RequestEnvelope.builder()
                        .withRequest(IntentRequest.builder()
                                .withIntent(Intent.builder()
                                        .withName("AMAZON.CancelIntent")
                                        .build())
                                .build())
                        .build()
                ).build();
        assertTrue(handler.canHandle(input));
        Optional<Response> responseOptional = handler.handle(input);
        assertTrue(responseOptional.isPresent());
        Response response = responseOptional.get();
        assertTrue(response.getOutputSpeech() instanceof SsmlOutputSpeech);
        assertEquals("<speak>Thank you for using Pet Clinic</speak>", ((SsmlOutputSpeech) response.getOutputSpeech()).getSsml());
        assertTrue(response.getCard() instanceof SimpleCard);
        assertEquals("PetClinic", ((SimpleCard) response.getCard()).getTitle());
        assertEquals("Thank you for using Pet Clinic", ((SimpleCard) response.getCard()).getContent());
        assertTrue(response.getShouldEndSession());
    }
}
