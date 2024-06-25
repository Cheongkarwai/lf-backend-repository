package com.lfhardware.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDTO extends UserDTO {

//    private String username;
//
//    private String password;
//
//    private Set<RoleDTO> roles = new HashSet<>();
//
//    private UserProfileDTO profile;

    private boolean disabled;

//    @JsonProperty("email_verified")
//    @Override
//    public Boolean isEmailVerified(){
//        return super.emailVerified;
//    }
}
