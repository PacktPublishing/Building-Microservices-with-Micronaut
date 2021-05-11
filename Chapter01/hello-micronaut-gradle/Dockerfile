FROM openjdk:14-alpine
COPY build/libs/com.packtpub.micronaut.hellomicronautgradle-*-all.jar hello-micronaut-gradle.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "hello-micronaut-gradle.jar"]
