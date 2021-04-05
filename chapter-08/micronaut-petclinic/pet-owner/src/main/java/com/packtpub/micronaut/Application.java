package com.packtpub.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.server.EmbeddedServer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@OpenAPIDefinition(
    info = @Info(
        title = "pet-owner-service",
        version = "1.0",
        description = "Pet Owner APIs"
    )
)
@Singleton
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = Micronaut.run(Application.class, args);
        logApplicationStartup(applicationContext);
    }

    private static void logApplicationStartup(ApplicationContext context) {
        EmbeddedServer server = context.getBean(EmbeddedServer.class);
        ApplicationConfiguration application = context.getBean(ApplicationConfiguration.class);
        String protocol = server.getScheme();
        int serverPort = server.getPort();
        String hostAddress = server.getHost();

        log.info(
                "\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t\t{}://localhost:{}\n\t" +
                        "External: \t\t{}://{}:{}" +
                "\n----------------------------------------------------------",
                application.getName().orElse(null),
                protocol,
                serverPort,
                protocol,
                hostAddress,
                serverPort
        );
    }
}
