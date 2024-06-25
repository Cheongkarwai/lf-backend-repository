package com.lfhardware.auth.service;

import com.lfhardware.auth.domain.User;
import com.lfhardware.auth.dto.*;
import com.lfhardware.shared.Pageable;
import io.vertx.ext.mail.MailResult;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IUserService  {

    Mono<Void> save(UserDTO userDTO);

    Mono<UserDTO> save(UserAccountDTO userAccountDTO);

    Mono<Void> update(String username, UserProfileDTO userProfileDTO);

    Mono<Void> update(String id, UserRepresentation userRepresentation);

    Mono<UserDTO> linkSocialAccount(UserAccountDTO userAccountDTO);

    Mono<UserRepresentation> findById(String id);

    Mono<Void> updateUserRoleById(String id, List<RoleRepresentation> roleRepresentations);

    Mono<UserAccountDTO> findUserAccountByUsername(String username);

    Mono<UserRoleDTO> findUserRoleById(String username);

    Mono<MailResult> otpLogin(OtpLoginDTO otpLoginDTO);

    Mono<User> changePassword(ChangePasswordDTO changePasswordDTO);

    Mono<MailResult> forgotPassword(PasswordRecoveryDTO passwordRecoveryDTO);

    Mono<MailResult> verifyEmail(String email);

    Mono<Void> saveServiceProviderAccount(ServiceProviderAccountDTO serviceProviderAccountDTO);

    Mono<UserDTO> findByPhoneNumber(String phoneNumber);

    Mono<UserDTO> findByEmailAddress(String emailAddress);

    Mono<Pageable<UserDTO>> findAll(UserPageRequest userPageRequest);

    Mono<List<RoleDTO>> findAllRoles();

    Mono<List<RoleDTO>> findAllRolesById(String id);

    Mono<Long> count();

    Mono<List<DailyUserStat>> findDailyUserCount(Integer days);

    Mono<UserDTO> findCurrentlyLoggedInUser();
}
