package com.lfhardware.user.mapper;

import com.lfhardware.user.dto.AdminDTO;
import com.lfhardware.user.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "username", source = "userRepresentation.username")
    AdminDTO mapToAdminDTO(UserRepresentation userRepresentation);

    @Mapping(target = "username", source = "userRepresentation.username")
    UserDTO mapToUserDTO(UserRepresentation userRepresentation);

    @Mapping(target = ".", source = "userDTO")
    UserRepresentation mapToUserRepresentation(UserDTO userDTO);
}
