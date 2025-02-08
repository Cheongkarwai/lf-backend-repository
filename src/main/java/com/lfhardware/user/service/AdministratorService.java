package com.lfhardware.user.service;

//@Service
//public class AdministratorService extends UserService {
//
//    private final KeycloakAdminAdapter keycloakAdapter;
//
//    private final UserFactory userFactory;
//
//    private final UserMapper userMapper;
//
//    private final RoleMapper roleMapper;
//
//
//    public AdministratorService(KeycloakAdminAdapter keycloakAdapter,
//                                UserFactory userFactory,
//                                UserMapper userMapper,
//                                RoleMapper roleMapper) {
//        this.keycloakAdapter = keycloakAdapter;
//        this.userFactory = userFactory;
//        this.userMapper = userMapper;
//        this.roleMapper = roleMapper;
//    }
//
//    @Override
//    public Flux<UserDTO> findAll(Boolean briefRepresentation, String email, Boolean emailVerified,
//                                 Boolean enabled, Boolean exact, String first, String firstName,
//                                 String idpAlias, String lastName, String max, String q, String search,
//                                 String username) {
//        return keycloakAdapter.findAllUsers(briefRepresentation, email, emailVerified,
//                        enabled, exact, first, firstName, idpAlias, lastName, max,
//                        q, search, username)
//                .map(userRepresentation -> userFactory.createUser(UserType.USER, userRepresentation))
//                .log();
//    }
//
//    @Override
//    public Mono<Void> save(UserDTO userDTO) {
//        return keycloakAdapter.createUser(userMapper.mapToUserRepresentation(userDTO));
//    }
//
//    @Override
//    public Flux<RoleDTO> findAvailableRoles(String username) {
//        return findAll(false, null, null,
//                null, true, null, null, null, null, null,
//                null, null, username)
//                .flatMap(userRepresentation -> keycloakAdapter.findAllUserRealmRolesAvailable(userRepresentation.getId()))
//                .map(roleMapper::mapToRoleDTO);
//    }
//
//
//    @Override
//    public Mono<UserDTO> update(String id, UserDTO userDTO) {
//        return null;
//    }
//
//    @Override
//    public Mono<Void> remove(String id) {
//        return null;
//    }
//
//    @Override
//    public Mono<Void> assignRoles(String username, String roleId) {
//        return findAll(false, null, null,
//                null, true, null, null, null, null, null,
//                null, null, username)
//                .flatMap(userRepresentation -> keycloakAdapter.findAllUserRealmRolesAvailable(userRepresentation.getId())
//                        .filter(roleRepresentation -> roleRepresentation.getId().equals(roleId))
//                        .collectList()
//                        .flatMap(roleRepresentations -> keycloakAdapter.assignRealmRoles(userRepresentation.getId(), roleRepresentations))).then();
//    }
//
//    @Override
//    public UserType getUserType() {
//        return UserType.ADMINISTRATOR;
//    }
//
//}
