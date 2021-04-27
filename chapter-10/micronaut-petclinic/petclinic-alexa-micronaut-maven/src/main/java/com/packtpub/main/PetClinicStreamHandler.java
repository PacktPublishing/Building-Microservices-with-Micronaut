package com.packtpub.main;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.packtpub.alexa.*;

public class PetClinicStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        Skill build = Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new PetClinicWelcomeIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler())
                .withSkillId("amzn1.ask.skill.de392a0b-0a95-451a-a615-dcba1f9a23c6")
                .build();
        return build;
    }
    public PetClinicStreamHandler() {
        super(getSkill());
    }
}
