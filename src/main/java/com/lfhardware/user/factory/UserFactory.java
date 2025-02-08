package com.lfhardware.user.factory;

import com.lfhardware.user.dto.*;
import com.lfhardware.user.mapper.UserMapper;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFactory {

    private final UserMapper userMapper;

    public UserFactory(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserDTO createUser(UserType userType, UserRepresentation userRepresentation) {
        return switch (userType) {
            case USER-> userMapper.mapToUserDTO(userRepresentation);
            case ADMINISTRATOR -> userMapper.mapToAdminDTO(userRepresentation);
            case GUEST -> new GuestDTO();
            case REGULAR_USER -> new RegularUserDTO();
        };
    }

}
