package com.lfhardware.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.keycloak.representations.idm.AbstractUserRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends AbstractUserRepresentation {

//    private String id;
//
//    private String username;

//    private String password;

//    private String stripeId;

    private ProfileDTO profile;

    private Set<RoleDTO> roles = new HashSet<>();

    @JsonProperty("email_verified")
    @Override
    public Boolean isEmailVerified(){
        return super.emailVerified;
    }

    @JsonProperty("first_name")
    @Override
    public String getFirstName(){
        return super.firstName;
    }

    @JsonProperty("last_name")
    @Override
    public String getLastName(){
        return super.lastName;
    }

//    @JsonProperty("first_name")
//    private String firstName;
//
//    @JsonProperty("last_name")
//    private String lastName;

    @JsonProperty("first_time_login")
    private boolean firstTimeLogin;

}
