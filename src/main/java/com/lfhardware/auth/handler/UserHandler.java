package com.lfhardware.auth.handler;


import com.lfhardware.auth.dto.UserDTO;
import com.lfhardware.auth.dto.UserRoleDTO;
import com.lfhardware.auth.service.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    private final IUserService userService;

    public UserHandler(IUserService userService){
        this.userService = userService;
    }

    public Mono<ServerResponse> findUser(ServerRequest serverRequest){
        return ServerResponse.ok()
                        .body(userService.findById(serverRequest.pathVariable("username")), UserDTO.class);
    }

    public Mono<ServerResponse> findUserRole(ServerRequest serverRequest){
        return ServerResponse.ok()
                .body(userService.findUserRoleById(serverRequest.pathVariable("username")), UserRoleDTO.class);
    }
}
