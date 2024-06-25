package com.lfhardware.account.service;

import com.lfhardware.account.dto.ResetPasswordInput;
import com.lfhardware.account.dto.UserCredentialsDTO;
import com.lfhardware.auth.dto.Role;
import com.lfhardware.auth.dto.RoleDTO;
import com.lfhardware.auth.dto.UserDTO;
import com.lfhardware.auth.dto.UserProfileDTO;
import com.lfhardware.keycloak.rest.account.dto.OTPQrCodeDTO;
import com.lfhardware.keycloak.rest.account.dto.OtpDTO;
import com.stripe.model.Account;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IAccountService {

    Mono<UserDTO> findCurrentlyLoggedInUserAccount();


    Mono<Account> findStripeAccount(String accountId);


    Mono<UserRepresentation> findById(String id);

    Mono<List<UserCredentialsDTO>> findAllUserCredentials();

    Mono<Void> resetPassword(ResetPasswordInput resetPasswordInput);

    Mono<OTPQrCodeDTO> generateOtpQrCode();

    Mono<Void> setupOTPVerification(OtpDTO otpDTO);


    Mono<Account> createStripeAccount();

    Mono<String> createStripeAccountOnboardingLink(String accountId);

    Mono<Void> updateCurrentlyLoggedInUserAccount(UserProfileDTO userProfileDTO);

    Mono<Void> updateAccountRoleById(String id, List<RoleRepresentation> roleRepresentations);



}
