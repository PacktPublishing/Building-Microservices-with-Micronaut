FROM openjdk:14-alpine
COPY target/pet-clinic-*.jar pet-clinic.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "pet-clinic.jar"]