package com.epam.jmp.rest.web;

import com.epam.jmp.rest.model.User;
import com.epam.jmp.rest.service.UserService;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.*;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

@Path("users")
@Component
public class UserResource {

    public static final String IMAGE_SERVER = "toDownload/";
    @Autowired
    private UserService userService;

    @POST
    @Consumes(APPLICATION_XML)
    @Produces(APPLICATION_JSON)
    public Long createUser(User user) {
        return userService.createUser(user);
    }

    @PUT
    @Path("{userId}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Long updateUser(User user, @PathParam("userId") Long userId) {
        user.setId(userId);
        userService.updateUser(user);
        return userId;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @DELETE
    @Path("{userId}")
    @Produces(APPLICATION_JSON)
    public void deleteUser(@PathParam("userId") Long userId) {
        userService.delete(userId);
    }

    @POST
    @Path("/logo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadLogo(@FormDataParam("file") FormDataBodyPart bodyPart) {
        String uploadFilePath = null;
        try {
            String fileName = bodyPart.getContentDisposition().getFileName();
            uploadFilePath = writeToFileServer(bodyPart.getValueAs(InputStream.class), fileName);
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        return uploadFilePath;
    }

    @GET
    @Path("/logo")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response downloadLogo() {
        File download = new File("toDownload/userLogo.png");
        return Response.ok(download)
                .header("Content-Disposition", "attachment; filename=userLogo.png")
                .build();
    }

    private String writeToFileServer(InputStream inputStream, String fileName) throws IOException {
        String qualifiedUploadFilePath = IMAGE_SERVER + fileName;
        try (OutputStream outputStream = new FileOutputStream(new File(qualifiedUploadFilePath))) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return qualifiedUploadFilePath;
    }
}
