package com.lfhardware.auth.service;

import com.lfhardware.auth.dto.UserAccountDTO;
import com.lfhardware.auth.dto.UserDTO;
import com.lfhardware.auth.dto.UserRoleDTO;
import reactor.core.publisher.Mono;

public interface IUserService {

    Mono<Void> save(UserDTO userDTO);

    Mono<Void> save(UserAccountDTO userAccountDTO);

    Mono<UserDTO> findById(String username);

    Mono<UserRoleDTO> findUserRoleById(String username);
}
