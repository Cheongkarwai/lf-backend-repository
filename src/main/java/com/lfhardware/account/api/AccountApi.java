package com.lfhardware.account.api;

import com.lfhardware.account.dto.ResetPasswordInput;
import com.lfhardware.account.service.IAccountService;
import com.lfhardware.auth.dto.UserProfileDTO;
import com.lfhardware.keycloak.rest.account.dto.OtpDTO;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class AccountApi {

    private final IAccountService accountService;

    public AccountApi(IAccountService accountService) {
        this.accountService = accountService;
    }

    public Mono<ServerResponse> findCurrentlyLoggedInUserAccount(ServerRequest serverRequest) {
        return accountService.findCurrentlyLoggedInUserAccount()
                .flatMap(account -> ServerResponse.ok().bodyValue(account));
    }

    public Mono<ServerResponse> updateCurrentlyLoggedInUserAccount(ServerRequest serverRequest){
        return serverRequest.bodyToMono(UserProfileDTO.class)
                .flatMap(accountService::updateCurrentlyLoggedInUserAccount)
                .then(Mono.defer(() -> ServerResponse.noContent().build()));
    }

    //Create Stripe Account
//    public Mono<ServerResponse> createStripeConnectAccount(ServerRequest serverRequest) {
//        return accountService.createStripeAccount()
//                .flatMap(link -> ServerResponse.ok().bodyValue(link));
//    }

    public Mono<ServerResponse> test(ServerRequest serverRequest) {
        return ServerResponse.permanentRedirect(URI.create("http://localhost:8090")).build();
    }

    public Mono<ServerResponse> resetPassword(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ResetPasswordInput.class)
                .flatMap(accountService::resetPassword)
                .flatMap(e -> ServerResponse.ok().build())
                .onErrorResume(err -> ServerResponse.badRequest().bodyValue(err))
                .log();
    }

    public Mono<ServerResponse> generateOtpQrCode(ServerRequest serverRequest) {
        return accountService.generateOtpQrCode()
                .flatMap(otpQrCodeDTO -> ServerResponse.ok().bodyValue(otpQrCodeDTO));
    }

    public Mono<ServerResponse> setupOtpVerification(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(OtpDTO.class)
                .flatMap(accountService::setupOTPVerification)
                .then(Mono.defer(() -> ServerResponse.ok().build()));
    }

    public Mono<ServerResponse> findAllUserCredentials(ServerRequest serverRequest) {
        return accountService.findAllUserCredentials()
                .flatMap(userCredentials -> ServerResponse.ok().bodyValue(userCredentials));
    }

}
