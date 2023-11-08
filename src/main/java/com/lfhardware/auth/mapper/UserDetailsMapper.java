package com.lfhardware.auth.mapper;

import com.lfhardware.auth.domain.Address;
import com.lfhardware.auth.domain.Profile;
import com.lfhardware.auth.domain.User;
import com.lfhardware.auth.dto.UserAccountDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsMapper{

    public static UserDetails map(User user){
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .disabled(false)
                .authorities( user.getUserRoles().stream().map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName()))
                        .collect(Collectors.toList()))
                .credentialsExpired(false)
                .build();

    }


}