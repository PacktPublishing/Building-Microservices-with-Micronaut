

## Type 1
com.packtpub.main.PetClinicStreamHandler

## Type 2
As handler specify io.micronaut.function.aws.alexa.AlexaFunction. You don’t need to create a class which extends PetClinicStreamHandler, AlexaFunction takes care of adding request handlers interceptors etc.

com.packtpub.main.AlexaFunction

## Type 3
io.micronaut.function.aws.proxy.MicronautLambdaHandler

## Compiling and Creating Jar file for Amazon AWS Lambda
mvn package

## Compiling and Creating Jar file for Amazon AWS Lambda
mvn assembly:assembly -DdescriptorId=jar-with-dependencies package
