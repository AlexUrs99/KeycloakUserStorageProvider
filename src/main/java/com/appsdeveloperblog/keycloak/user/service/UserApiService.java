package com.appsdeveloperblog.keycloak.user.service;

import com.appsdeveloperblog.keycloak.user.controller.dto.VerifyPasswordResponse;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import java.util.List;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON_VALUE)
public interface UserApiService {

    @GET
    List<UserPojo> getAllUsers();

    @GET
    @Path("/{username}")
    UserPojo getUser(@PathParam("username") String username);

    @POST
    @Path("/{username}/verify-password")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    VerifyPasswordResponse verifyUserPassword(@PathParam("username") String username,
                                              String password);

}