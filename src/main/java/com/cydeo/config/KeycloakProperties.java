package com.cydeo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//what is the purpose to create separate class (Keycloak Properties)
//Because in different places in my application we can use this field without
//redeclared this field, by using this class I can use this fields anywhere, that is the whole purpose
//we are going to use a lot this structure in the microservice also
// why we use @Value

@Component
@Getter
@Setter
public class KeycloakProperties {

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    @Value("${master.user}")
    private String masterUser;
    @Value("${master.user.password}")
    private String masterUserPswd;
    @Value("${master.realm}")
    private String masterRealm;
    @Value("${master.client}")
    private String masterClient;

}