package com.lfhardware.auth.mapper;

import com.lfhardware.auth.domain.User;
import com.lfhardware.auth.dto.UserAccountDTO;
import com.lfhardware.auth.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = RoleMapper.class)
public interface UserRecordMapper {
    @Mappings({
           // @Mapping(target = "stripeId", source = "stripeId"),
            @Mapping(target = "username",source = "username"),
            @Mapping(target = "roles", source = "userRoles"),
            @Mapping(target = "profile", source = "profile")
    })
    UserDTO mapToUserDTO(User user);


    @Mappings({
           // @Mapping(target = "stripeId", source = "stripeId"),
            @Mapping(target = "username",source = "username"),
            @Mapping(target = "roles", source = "userRoles"),
            @Mapping(target = "profile", source = "profile")
    })
    UserAccountDTO mapToUserAccountDTO(User user);


    @Mappings({
//            @Mapping(target = "uid", source = "id"),
            @Mapping(target = "username", source = "username"),
    })
    UserDTO mapToUserDTO(UserRepresentation userRepresentation);

}
