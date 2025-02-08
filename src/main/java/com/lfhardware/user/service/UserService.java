package com.lfhardware.user.service;

import com.lfhardware.auth.mapper.RoleMapper;
import com.lfhardware.keycloak.KeycloakAdminAdapter;
import com.lfhardware.keycloak.KeycloakProtectionAdapter;
import com.lfhardware.keycloak.KeycloakUmaAdapter;
import com.lfhardware.authorization.permission.service.IPermissionService;
import com.lfhardware.authorization.scope.service.IScopeService;
import com.lfhardware.authorization.resource.service.ResourceService;
import com.lfhardware.authorization.resource.dto.ResourceType;
import com.lfhardware.user.dto.RoleDTO;
import com.lfhardware.user.dto.UserDTO;
import com.lfhardware.user.dto.UserType;
import com.lfhardware.user.factory.UserFactory;
import com.lfhardware.user.mapper.UserMapper;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.keycloak.representations.idm.authorization.UserPolicyRepresentation;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final KeycloakAdminAdapter keycloakAdapter;

    private final UserFactory userFactory;

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final KeycloakProtectionAdapter keycloakProtectionAdapter;

    private final KeycloakUmaAdapter keycloakUmaAdapter;

    private final IPermissionService permissionService;

    private final IScopeService scopeService;
    private final ResourceService resourceService;

    public UserService(KeycloakAdminAdapter keycloakAdapter,
                       UserFactory userFactory,
                       UserMapper userMapper,
                       RoleMapper roleMapper,
                       KeycloakProtectionAdapter keycloakProtectionAdapter,
                       KeycloakUmaAdapter keycloakUmaAdapter,
                       IPermissionService permissionService,
                       IScopeService scopeService, ResourceService resourceService) {
        this.keycloakAdapter = keycloakAdapter;
        this.userFactory = userFactory;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.keycloakProtectionAdapter = keycloakProtectionAdapter;
        this.keycloakUmaAdapter = keycloakUmaAdapter;
        this.permissionService = permissionService;
        this.scopeService = scopeService;
        this.resourceService = resourceService;
    }

    @Override
    public Flux<UserDTO> findAll(Boolean briefRepresentation, String email, Boolean emailVerified,
                                 Boolean enabled, Boolean exact, String first, String firstName,
                                 String idpAlias, String lastName, String max, String q, String search,
                                 String username) {
        return keycloakAdapter.findAllUsers(briefRepresentation, email, emailVerified,
                        enabled, exact, first, firstName, idpAlias, lastName, max,
                        q, search, username)
                .map(userRepresentation -> userFactory.createUser(UserType.USER, userRepresentation));
    }

    @Override
    public Mono<UserDTO> findByUsername(String username) {
        return keycloakAdapter.findAllUsers(null, null, null,
                        null, true, null, null, null, null, null,
                        null, null, username)
                .map(userRepresentation -> userFactory.createUser(UserType.USER, userRepresentation))
                .take(1).singleOrEmpty();
    }

    @Override
    public Mono<Void> save(UserDTO userDTO) {
        UserRepresentation userRepresentation = userMapper.mapToUserRepresentation(userDTO);
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue("cheong1234");
        userRepresentation.setCredentials(List.of(credentialRepresentation));
        return keycloakAdapter.createUser(userRepresentation)
                .then(Mono.defer(() -> {
                    return Mono.zip(ReactiveSecurityContextHolder.getContext(),
                                    scopeService.findAll("0", "11", true).collectList())
                            .flatMap(context -> {
                                if (context.getT1().getAuthentication().getCredentials() instanceof Jwt jwt) {
                                    ResourceRepresentation resourceRepresentation = new ResourceRepresentation();
                                    String uuid = UUID.randomUUID().toString();
                                    System.out.println(uuid);
                                    resourceRepresentation.setId(uuid);
                                    System.out.println(userRepresentation.getUsername());
                                    resourceRepresentation.setDisplayName("User " + userRepresentation.getUsername());
                                    resourceRepresentation.setName(userRepresentation.getUsername());
                                    resourceRepresentation.setOwnerManagedAccess(true);
                                    resourceRepresentation.setOwner(jwt.getSubject());
                                    resourceRepresentation.setType(ResourceType.USER +":" + userRepresentation.getUsername());
                                    resourceRepresentation.setUris(Set.of("/api/v1/users/" + userRepresentation.getUsername()));
                                    resourceRepresentation.setScopes(new HashSet<>(context.getT2()));

                                    return resourceService.create(resourceRepresentation)
                                            .then(Mono.defer(() -> {
                                                return keycloakProtectionAdapter.findResourceSet("/api/v1/users/" + userRepresentation.getUsername(), true)
                                                        .single()
                                                        .flatMap(savedResourceRepresentation -> {
                                                            UserPolicyRepresentation userPolicyRepresentation = new UserPolicyRepresentation();
                                                            userPolicyRepresentation.setUsers(Set.of(jwt.getSubject()));
                                                            userPolicyRepresentation.setDescription("Hi");
                                                            userPolicyRepresentation.setName("Hi");
                                                            userPolicyRepresentation.setScopes(Set.of("read", "write"));
                                                            return permissionService.createPolicy(savedResourceRepresentation.getId(), userPolicyRepresentation);
                                                        });
                                            }));
                                }
                                return Mono.error(new RuntimeException("Error when saving resource"));
                            });
                }));
    }

    @Override
    public Flux<RoleDTO> findAvailableRoles(String username) {
        return findAll(false, null, null,
                null, true, null, null, null, null, null,
                null, null, username)
                .flatMap(userRepresentation -> keycloakAdapter.findAllUserRealmRolesAvailable(userRepresentation.getId()))
                .map(roleMapper::mapToRoleDTO);
    }


    @Override
    public Mono<UserDTO> update(String id, UserDTO userDTO) {
        return keycloakAdapter.updateUser(id, userMapper.mapToUserRepresentation(userDTO));
    }

    @Override
    public Mono<Void> remove(String id) {
        return null;
    }

    @Override
    public UserType getUserType() {
        return null;
    }

    @Override
    public Mono<Void> assignRoles(String username, String roleId) {
        return findAll(false, null, null,
                null, true, null, null, null, null, null,
                null, null, username)
                .flatMap(userRepresentation -> keycloakAdapter.findAllUserRealmRolesAvailable(userRepresentation.getId())
                        .filter(roleRepresentation -> roleRepresentation.getId().equals(roleId))
                        .collectList()
                        .flatMap(roleRepresentations -> keycloakAdapter.assignRealmRoles(userRepresentation.getId(), roleRepresentations))).then();
    }
}
