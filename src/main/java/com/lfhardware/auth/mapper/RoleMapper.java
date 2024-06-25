package com.lfhardware.auth.mapper;

import com.lfhardware.auth.domain.Role;
import com.lfhardware.auth.domain.UserRole;
import com.lfhardware.auth.dto.RoleDTO;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.springframework.security.core.GrantedAuthority;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    @Mappings({
            @Mapping(target = "id", source = "role.id"),
            @Mapping(target = "name", source = "role.name")
    })
    RoleDTO mapToRoleDTO(UserRole userRole);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name")
    })
    RoleDTO mapToRoleDTO(Role role);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name")
    })
    Role mapToRole(RoleDTO roleDTO);



    @Mapping(target = "name", source = "authority")
    RoleDTO mapToRoleDTO(GrantedAuthority grantedAuthority);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "id", source = "id")
    RoleDTO mapToRoleDTO(RoleRepresentation roleRepresentation);
}
