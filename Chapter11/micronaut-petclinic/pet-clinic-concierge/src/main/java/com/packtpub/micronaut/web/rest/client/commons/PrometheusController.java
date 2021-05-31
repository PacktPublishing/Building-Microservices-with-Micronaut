package com.packtpub.micronaut.web.rest.client.commons;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micronaut.configuration.metrics.annotation.RequiresMetrics;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import javax.inject.Inject;

@RequiresMetrics
@Controller("/metrics")
@Secured(SecurityRule.IS_ANONYMOUS)
public class PrometheusController {

    private final PrometheusMeterRegistry prometheusMeterRegistry;

    @Inject
    public PrometheusController(PrometheusMeterRegistry prometheusMeterRegistry) {
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }

    @Get
    @Produces("text/plain")
    public String metrics() {
        return prometheusMeterRegistry.scrape();
    }
}
