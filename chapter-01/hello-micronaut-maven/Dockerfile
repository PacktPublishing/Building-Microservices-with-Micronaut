FROM openjdk:14-alpine
COPY target/com.packtpub.micronaut.hellomicronautmaven-*-all.jar hello-micronaut-maven-0.1.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "hello-micronaut-maven-0.1.jar"]
