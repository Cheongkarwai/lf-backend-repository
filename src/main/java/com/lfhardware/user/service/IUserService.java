package com.lfhardware.user.service;

import com.lfhardware.user.dto.RoleDTO;
import com.lfhardware.user.dto.UserDTO;
import com.lfhardware.user.dto.UserType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {

    Flux<UserDTO> findAll(Boolean briefRepresentation, String email, Boolean emailVerified,
                          Boolean enabled, Boolean exact, String first, String firstName,
                          String idpAlias, String lastName, String max, String q, String search,
                          String username);

    Mono<UserDTO> findByUsername(String username);

    Mono<Void> save(UserDTO userDTO);

    Mono<UserDTO> update(String id, UserDTO userDTO);

    Mono<Void> remove(String id);

    UserType getUserType();

    Flux<RoleDTO> findAvailableRoles(String username);

    Mono<Void> assignRoles(String username, String roleId);


//    Mono<Void> save(UserDTO userDTO);
//
//    Mono<UserDTO> save(UserAccountDTO userAccountDTO);
//
//    Mono<Void> update(String username, UserProfileDTO userProfileDTO);
//
//    Mono<Void> update(String id, UserRepresentation userRepresentation);
//
//    Mono<UserDTO> linkSocialAccount(UserAccountDTO userAccountDTO);
//
//    Mono<UserRepresentation> findById(String id);
//
//    Mono<Void> updateUserRoleById(String id, List<RoleRepresentation> roleRepresentations);
//
//    Mono<UserAccountDTO> findUserAccountByUsername(String username);
//
//    Mono<UserRoleDTO> findUserRoleById(String username);
//
//    Mono<MailResult> otpLogin(OtpLoginDTO otpLoginDTO);
//
//    Mono<User> changePassword(ChangePasswordDTO changePasswordDTO);
//
//    Mono<MailResult> forgotPassword(PasswordRecoveryDTO passwordRecoveryDTO);
//
//    Mono<MailResult> verifyEmail(String email);
//
//    Mono<Void> saveServiceProviderAccount(ServiceProviderAccountDTO serviceProviderAccountDTO);
//
//    Mono<UserDTO> findByPhoneNumber(String phoneNumber);
//
//    Mono<UserDTO> findByEmailAddress(String emailAddress);
//
//    Mono<Pageable<UserDTO>> findAll(UserPageRequest userPageRequest);
//
//    Mono<List<RoleDTO>> findAllRoles();
//
//    Mono<List<RoleDTO>> findAllRolesById(String id);
//
//    Mono<Long> count();
//
//    Mono<List<DailyUserStat>> findDailyUserCount(Integer days);
//
//    Mono<UserDTO> findCurrentlyLoggedInUser();

//    public UserType getUserType(List<String> roles) {
//
//        System.out.println(roles);
//        if (roles == null) {
//            return UserType.GUEST;
//        }
//
//        for (String role : roles) {
//            if (role.equals(UserType.ADMINISTRATOR.name())) {
//                return UserType.ADMINISTRATOR;
//            } else if (role.equals(UserType.REGULAR_USER.name())) {
//                return UserType.REGULAR_USER;
//            }
//        }
//        return UserType.GUEST;
//    }
}
