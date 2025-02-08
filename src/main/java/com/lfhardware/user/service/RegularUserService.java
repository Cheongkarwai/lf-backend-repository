package com.lfhardware.user.service;

//@Service
//public class RegularUserService extends UserService {
//
//    private final KeycloakAdminAdapter keycloakAdapter;
//
//    private final UserFactory userFactory;
//
//    public RegularUserService(KeycloakAdminAdapter keycloakAdapter,
//                              UserFactory userFactory) {
//        this.keycloakAdapter = keycloakAdapter;
//        this.userFactory = userFactory;
//    }
//
//    @Override
//    public Flux<UserDTO> findAll(Boolean briefRepresentation, String email, Boolean emailVerified,
//                                 Boolean enabled, Boolean exact, String first, String firstName,
//                                 String idpAlias, String lastName, String max, String q, String search,
//                                 String username) {
//        return keycloakAdapter.findAllUsers(briefRepresentation, email, emailVerified,
//                        enabled, exact, first, firstName, idpAlias, lastName, max ,
//                        q, search, username)
//                .map(userRepresentation -> userFactory.createUser(UserType.USER, userRepresentation))
//                .log();
//    }
//
//    @Override
//    public UserType getUserType() {
//        return UserType.REGULAR_USER;
//    }
//}
