package com.lfhardware.account.api;

import com.lfhardware.account.service.IAccountService;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

//    /**
//     * @param serverRequest - request object
//     * @return Mono<ServerResponse>
//     *
//     */
//    public Mono<ServerResponse> findCurrentlyLoggedInUserAccount(ServerRequest serverRequest) {
//        return accountService.findCurrentlyLoggedInUserAccount()
//                .flatMap(account -> ServerResponse.ok().bodyValue(account));
//    }
//
//    /**
//     * @param serverRequest - request object
//     * @return Mono<ServerResponse>
//     */
//    public Mono<ServerResponse> updateCurrentlyLoggedInUserAccount(ServerRequest serverRequest){
//        return serverRequest.bodyToMono(UserProfileDTO.class)
//                .flatMap(accountService::updateCurrentlyLoggedInUserAccount)
//                .then(Mono.defer(() -> ServerResponse.noContent().build()));
//    }
//
//    //Create Stripe Account
////    public Mono<ServerResponse> createStripeConnectAccount(ServerRequest serverRequest) {
////        return accountService.createStripeAccount()
////                .flatMap(link -> ServerResponse.ok().bodyValue(link));
////    }
//
//
//    /**
//     * @param serverRequest - request object
//     * @return Mono<ServerResponse>
//     */
//    public Mono<ServerResponse> resetPassword(ServerRequest serverRequest) {
//        return serverRequest.bodyToMono(ResetPasswordInput.class)
//                .flatMap(accountService::resetPassword)
//                .then(Mono.defer(() -> ServerResponse.ok().build()))
//                .onErrorResume(err -> ServerResponse.badRequest().bodyValue(err))
//                .log();
//    }
//
//    /**
//     * @param serverRequest - request object
//     * @return Mono<ServerResponse>
//     */
//    public Mono<ServerResponse> generateOtpQrCode(ServerRequest serverRequest) {
//        return accountService.generateOtpQrCode()
//                .flatMap(otpQrCodeDTO -> ServerResponse.ok().bodyValue(otpQrCodeDTO));
//    }
//
//    /**
//     * @param serverRequest - request object
//     * @return Mono<ServerResponse>
//     */
//    public Mono<ServerResponse> setupOtpVerification(ServerRequest serverRequest) {
//        return serverRequest.bodyToMono(OtpDTO.class)
//                .flatMap(accountService::setupOTPVerification)
//                .then(Mono.defer(() -> ServerResponse.ok().build()));
//    }
//
//    /**
//     * @param serverRequest - request object
//     * @return Mono<ServerResponse>
//     */
//    public Mono<ServerResponse> findAllUserCredentials(ServerRequest serverRequest) {
//        return accountService.findAllUserCredentials()
//                .flatMap(userCredentials -> ServerResponse.ok().bodyValue(userCredentials));
//    }

}
