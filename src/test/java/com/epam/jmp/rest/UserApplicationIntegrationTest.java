package com.epam.jmp.rest;

import com.epam.jmp.rest.model.User;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class UserApplicationIntegrationTest {

    private static HttpServer server;
    private static WebTarget target;

    @BeforeClass
    public static void setUp() throws Exception {
        server = UserApplication.startServer();
        Client c = ClientBuilder.newClient().register(MultiPartFeature.class);
        server.start();
        target = c.target(UserApplication.BASE_URI);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testCreateUser() {
        User user = new User("firstName", "lastName", "login", "email");
        Long userId = target.path("users")
                .request()
                .post(Entity.entity(user, MediaType.APPLICATION_XML)).readEntity(Long.class);
        assertNotNull(userId);
        assertTrue(userId > 0);
    }

    @Test
    public void testUpdateUser() {
        User user = new User("firstName", "lastName", "login", "email");
        Long testUserId = 2L;
        Long userId = target.path("users/" + testUserId)
                .request()
                .put(Entity.entity(user, MediaType.APPLICATION_JSON))
                .readEntity(Long.class);
        assertEquals(userId, testUserId);
    }

    @Test
    public void testGetAllUsers() {
        List users = target.path("users")
                .request()
                .get()
                .readEntity(List.class);
        assertNotNull(users);
        assertTrue(users.size() > 0);
    }

    @Test
    public void testDeleteUser() {
        Long testUserId = 100L;
        int status = target.path("users").path(String.valueOf(testUserId))
                .request()
                .delete().getStatus();
        assertEquals(204, status);
    }

    @Test
    public void testUploadUserLogo() throws Exception {
        WebTarget t = target.path("users").path("logo");

        FileDataBodyPart filePart = new FileDataBodyPart("file",
                new File("toUpload/userLogo.png"));
        filePart.setContentDisposition(
                FormDataContentDisposition.name("file")
                        .fileName("userLogo.png").build());

        MultiPart multipartEntity = new FormDataMultiPart()
                .bodyPart(filePart);

        Response response = t.request().post(
                Entity.entity(multipartEntity, MediaType.MULTIPART_FORM_DATA));

        assertEquals(200, response.getStatus());
        assertEquals("toDownload/userLogo.png", response.readEntity(String.class));
    }

    @Test
    public void testDownloadUserLogo() throws Exception {
        WebTarget t = target.path("users").path("logo");
        Response response = t.request().get();

        assertEquals(200, response.getStatus());
        byte[] responseImage = response.readEntity(byte[].class);
        byte[] actualImage = Files.readAllBytes(Paths.get("toDownload/userLogo.png"));
        assertArrayEquals(actualImage, responseImage);
    }
}
