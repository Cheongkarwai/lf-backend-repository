package com.lfhardware.auth.mapper;

import com.lfhardware.auth.domain.Profile;
import com.lfhardware.auth.domain.User;
import com.lfhardware.auth.dto.ProfileDTO;
import com.lfhardware.auth.dto.RoleDTO;
import com.lfhardware.auth.dto.UserRoleDTO;

import java.util.function.Function;
import java.util.stream.Collectors;

public class UserRoleMapper {
    public static UserRoleDTO map(User user) {
        return  UserRoleDTO.builder()
                .username(user.getUsername())
                .profileDTO(ProfileDTO.builder().emailAddress(user.getProfile().getEmailAddress())
                        .phoneNumber(user.getProfile().getPhoneNumber())
                        .build())
                .roleDTOs(user.getUserRoles().stream()
                                            .map(userRole-> RoleDTO.builder()
                                                    .id(userRole.getRole().getId())
                                                    .name(userRole.getRole().getName())
                                                    .build())
                                            .collect(Collectors.toSet()))
                .build();
    }
}
