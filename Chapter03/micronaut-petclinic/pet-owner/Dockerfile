FROM openjdk:14-alpine
COPY target/pet-owner-*.jar pet-owner.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "pet-owner.jar"]