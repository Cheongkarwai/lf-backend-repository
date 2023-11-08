package com.lfhardware.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDTO extends UserProfileDTO {

    @JsonProperty("roles")
    private Set<RoleDTO> roleDTOs = new HashSet<>();
}
