package com.lfhardware.user.api;

import com.lfhardware.keycloak.KeycloakProtectionAdapter;
import com.lfhardware.user.dto.RoleDTO;
import com.lfhardware.user.dto.UserDTO;
import com.lfhardware.user.dto.UserType;
import com.lfhardware.user.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final IUserService userService;

    private final KeycloakProtectionAdapter keycloakAuthorizationAdapter;

    public UserController(IUserService userService,
                          KeycloakProtectionAdapter keycloakAuthorizationAdapter) {
        this.userService = userService;
        this.keycloakAuthorizationAdapter = keycloakAuthorizationAdapter;
    }

    @GetMapping
    public Flux<UserDTO> findAll(@RequestParam UserType role,
                                 @RequestParam(required = false) Boolean briefRepresentation,
                                 @RequestParam(required = false) String email,
                                 @RequestParam(required = false) Boolean emailVerified,
                                 @RequestParam(required = false) Boolean enabled,
                                 @RequestParam(required = false) Boolean exact,
                                 @RequestParam(required = false) String username,
                                 @RequestParam(required = false) String first,
                                 @RequestParam(required = false) String max,
                                 @RequestParam(required = false) String firstName,
                                 @RequestParam(required = false) String lastName,
                                 @RequestParam(required = false) String idpAlias,
                                 @RequestParam(required = false) String q,
                                 @RequestParam(required = false) String search) {
        return userService
                .findAll(briefRepresentation, email, emailVerified,
                        enabled, exact, first, firstName, idpAlias, lastName, max, q, search, username);
    }

    @GetMapping("/{username}")
    public Mono<UserDTO> find(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/test/1")
    public Mono<Void> test(){
        return Mono.empty();
    }

    @PostMapping
    public Mono<Void> save(@Valid @RequestBody UserDTO userDTO) {
        return userService.save(userDTO);
    }

    @GetMapping("/{username}/roles/available")
    public Flux<RoleDTO> findAvailableRoles(@PathVariable String username) {
        return userService.findAvailableRoles(username);
    }

    @GetMapping("/test")
    public Flux<?> test(@RequestParam(required = false) String uri,
                                             @RequestParam(required = false) Boolean matchingUri,
                                             @RequestParam(required = false) Boolean deep) {
        return keycloakAuthorizationAdapter.findResourceSet(uri,matchingUri);
    }

    @PostMapping("/{id}/mobile-number/validate")
    public Mono<Boolean> validateMobileNumber(@RequestBody String mobileNumber){
        return Mono.just(true);
    }
}
