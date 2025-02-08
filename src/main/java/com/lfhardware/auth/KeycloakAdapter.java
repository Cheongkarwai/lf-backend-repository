package com.lfhardware.auth;

import com.lfhardware.configuration.KeycloakProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

//@Component
//public class KeycloakAdapter {
//
////    private final Keycloak keycloak;
////
////    private final KeycloakProperties keycloakProperties;
////
////    private KeycloakAdapter(Keycloak keycloak, KeycloakProperties keycloakProperties){
////        this.keycloak = keycloak;
////        this.keycloakProperties = keycloakProperties;
////    }
////
////    public Flux<UserRepresentation> findAllUsers(){
////        return Flux.fromIterable(keycloak.realm(keycloakProperties.getRealm()).users().list());
////    }
////
////    public Flux<UserRepresentation> findAllUsersByRole(String role){
////        return Flux.fromIterable(keycloak.realm(keycloakProperties.getRealm()).roles().get(role).getUserMembers());
////    }
//}
