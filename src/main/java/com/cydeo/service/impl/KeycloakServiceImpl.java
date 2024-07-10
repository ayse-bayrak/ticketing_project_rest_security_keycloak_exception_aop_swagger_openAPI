package com.cydeo.service.impl;

import com.cydeo.config.KeycloakProperties;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.KeycloakService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.Arrays.asList;
import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;

@Service
public class KeycloakServiceImpl implements KeycloakService {


    private final KeycloakProperties keycloakProperties;
    // we did this class KeycloakProperties and put inside @Value variable
    //wherever class I needed I can inject it and I can use all these variable

    public KeycloakServiceImpl(KeycloakProperties keycloakProperties) {

        this.keycloakProperties = keycloakProperties;
    }

    @Override
    public Response userCreate(UserDTO userDTO) {
        //this is the service which is going to create the user in the keycloak

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        credential.setValue(userDTO.getPassWord());
        // whatever we do that action as a manual in the keycloak, we are doing through spring boot
        // CredentialRepresentation class is providing that information to me

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(userDTO.getUserName());
        keycloakUser.setFirstName(userDTO.getFirstName());
        keycloakUser.setLastName(userDTO.getLastName());
        keycloakUser.setEmail(userDTO.getUserName());
        keycloakUser.setCredentials(asList(credential));
        keycloakUser.setEmailVerified(true);
        keycloakUser.setEnabled(true);
        // whatever you see in keycloak add user screen


        Keycloak keycloak = getKeycloakInstance();
        //to be able to do some action in the keycloak we need to open an instance first in the keycloak, we need to open that instance
        // like transaction, if i want to do anything in the database we need to first open a transaction (we discussed this one, open transaction
        // hikari pool, it is collecting all the transaction for your beloved  )
        // ready code in this class at the end,

        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        //keycloakProperties--> there is one instance from one class called Keycloak Properties and inside this class there is a variable called realm and
        //I am trying to access the value of this variable with a getter method--> when you look at code like that, you can explain like this
        // and go to this class what is realm value oh i see @Value something we are reading from app properties, go to app properties and look key about realm
        // yes I see realm value is cydeo-dev so keycloakProperties.getRealm()= cydeo-dev
        UsersResource usersResource = realmResource.users();

        // Create Keycloak user
        Response result = usersResource.create(keycloakUser);

        String userId = getCreatedId(result);
        ClientRepresentation appClient = realmResource.clients()
                .findByClientId(keycloakProperties.getClientId()).get(0);

        RoleRepresentation userClientRole = realmResource.clients().get(appClient.getId()) //
                .roles().get(userDTO.getRole().getDescription()).toRepresentation();

        realmResource.users().get(userId).roles().clientLevel(appClient.getId())
                .add(List.of(userClientRole));


        keycloak.close();
        return result;
    }

    @Override
    public void delete(String userName) {

        Keycloak keycloak = getKeycloakInstance();

        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> userRepresentations = usersResource.search(userName);
        String uid = userRepresentations.get(0).getId();
        usersResource.delete(uid);

        keycloak.close();
    }

    private Keycloak getKeycloakInstance(){
        return Keycloak.getInstance(keycloakProperties.getAuthServerUrl(),
                keycloakProperties.getMasterRealm(), keycloakProperties.getMasterUser()
                , keycloakProperties.getMasterUserPswd(), keycloakProperties.getMasterClient());
    }
}