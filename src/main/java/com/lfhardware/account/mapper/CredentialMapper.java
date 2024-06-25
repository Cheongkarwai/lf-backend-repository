package com.lfhardware.account.mapper;

import com.lfhardware.account.dto.UserCredentialsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CredentialMapper {

    @Mapping(target = "label", source = "userLabel")
    UserCredentialsDTO mapToUserCredentialsDTO(CredentialRepresentation credentialRepresentation);
}
