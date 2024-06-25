package com.lfhardware.auth.mapper;

import com.lfhardware.auth.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDetailsMapper{


    public static UserDetails map(User user){
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
//                .password(user.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .disabled(false)
                .authorities( user.getUserRoles().stream().map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName()))
                        .collect(Collectors.toList()))
                .credentialsExpired(false)
                .build();

    }



}