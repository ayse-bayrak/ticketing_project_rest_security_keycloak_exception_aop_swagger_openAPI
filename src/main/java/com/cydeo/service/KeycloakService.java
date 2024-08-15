package com.cydeo.service;

import com.cydeo.dto.UserDTO;

import javax.ws.rs.core.Response; // comes dependency -javax
//we create this interface to create a user/delete a user in the keycloak


public interface KeycloakService {
    Response userCreate(UserDTO dto); // Response Class is providing us to create User in the keycloak you can access to that user
    void delete(String username);
}
