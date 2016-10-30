package com.epam.jmp.rest.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(RequestContextFilter.class);
        register(LoggingFeature.class);
        register(JacksonFeature.class);
        packages("org.glassfish.jersey.examples.multipart");
        register(MultiPartFeature.class);
    }
}