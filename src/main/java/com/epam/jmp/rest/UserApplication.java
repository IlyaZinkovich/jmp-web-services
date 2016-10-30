package com.epam.jmp.rest;

import com.epam.jmp.rest.config.JerseyConfig;
import com.epam.jmp.rest.config.SpringConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.net.URI;

public class UserApplication {

    public static final URI BASE_URI = URI.create("http://localhost:8282/myapp/");

    public static HttpServer startServer() {
        final ResourceConfig resourceConfig = new JerseyConfig().packages("com.epam.jmp.rest");
        resourceConfig.property("contextConfig", new AnnotationConfigApplicationContext(SpringConfig.class));
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig, false);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        server.start();
    }
}

