package com.lfhardware.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleDTO {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @JsonProperty("profile")
    private ProfileDTO profileDTO;

    @JsonProperty("roles")
    @NotNull(message = "Role must not be null")
    private Set<RoleDTO> roleDTOs = new HashSet<>();
}
