package com.lfhardware.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.auth.dto.ProfileDTO;
import com.lfhardware.auth.dto.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.AbstractUserRepresentation;

import java.util.HashSet;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends AbstractUserRepresentation {

    private ProfileDTO profile;

    private Set<RoleDTO> roles = new HashSet<>();

    @JsonProperty("email_verified")
    @Override
    public Boolean isEmailVerified() {
        return super.emailVerified;
    }

    @JsonProperty("first_name")
    @Override
    public String getFirstName() {
        return super.firstName;
    }

    @JsonProperty("last_name")
    @Override
    public String getLastName() {
        return super.lastName;
    }

    @JsonProperty("first_time_login")
    private boolean firstTimeLogin;

    private UserType userType;

}
