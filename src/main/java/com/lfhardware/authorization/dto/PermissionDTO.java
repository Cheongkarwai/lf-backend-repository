package com.lfhardware.authorization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PermissionDTO {

    @JsonProperty("resource_id")
    private String resourceId;

    @JsonProperty("resource_scopes")
    private List<String> resourceScopes;
}
