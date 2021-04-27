//package com.packtpub.main;
//
//
//import com.amazon.ask.AlexaSkill;
//import com.amazon.ask.SkillStreamHandler;
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
//import edu.umd.cs.findbugs.annotations.NonNull;
//import io.micronaut.aws.alexa.conf.AlexaEnvironment;
//import io.micronaut.context.ApplicationContext;
//import io.micronaut.context.ApplicationContextBuilder;
//import io.micronaut.context.ApplicationContextProvider;
//import io.micronaut.context.env.Environment;
//import io.micronaut.core.order.OrderUtil;
//import io.micronaut.function.aws.MicronautLambdaContext;
//
//import java.io.Closeable;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
///**
// * This is the base function you extend for Alexa skills support. For now you have to override apply but just call super() in it.
// * Your skill itself goes in implementing getSkill() and adding handlers for your intents.
// *
// * @author Ryan Vanderwerf
// * @author Graeme Rocher
// * @author sdelamo
// */
//public class AlexaFunction implements RequestStreamHandler, AutoCloseable, Closeable, ApplicationContextProvider {
//
//    protected ApplicationContext applicationContext;
//    protected SkillStreamHandler skillStreamHandler;
//
//    /**
//     * Default constructor.
//     */
//    public AlexaFunction() {
//        buildApplicationContext();
//        applicationContext.inject(this);
//        this.skillStreamHandler = new MicronautSkillStreamHandler(
//                applicationContext.getBeansOfType(AlexaSkill.class)
//                        .stream()
//                        .sorted(OrderUtil.COMPARATOR)
//                        .toArray(AlexaSkill[]::new));
//    }
//
//    /**
//     * Builds a new builder.
//     *
//     * @return The {@link ApplicationContextBuilder}
//     */
//    @SuppressWarnings("unchecked")
//    @NonNull
//    protected ApplicationContextBuilder newApplicationContextBuilder() {
//        return ApplicationContext.build(Environment.FUNCTION, MicronautLambdaContext.ENVIRONMENT_LAMBDA, AlexaEnvironment.ENV_ALEXA)
//                .eagerInitSingletons(true)
//                .eagerInitConfiguration(true);
//    }
//
//    /**
//     *
//     * @return returns the current application context or starts a new one.
//     */
//    @NonNull
//    protected ApplicationContext buildApplicationContext() {
//        if (applicationContext == null) {
//            applicationContext = newApplicationContextBuilder().build().start();
//        }
//        return applicationContext;
//    }
//
//    @Override
//    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
//        skillStreamHandler.handleRequest(input, output, context);
//    }
//
//    @Override
//    public void close() throws IOException {
//        if (applicationContext != null && applicationContext.isRunning()) {
//            applicationContext.close();
//        }
//    }
//
//    @Override
//    public ApplicationContext getApplicationContext() {
//        return this.applicationContext;
//    }
//}
