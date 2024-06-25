package com.lfhardware.email.service;
import com.lfhardware.email.dto.MailInformation;
import com.lfhardware.provider.domain.Status;
import io.vertx.ext.mail.MailResult;
import reactor.core.publisher.Mono;

public interface IEmailService {


    Mono<MailResult> sendOneTimeLoginPasscode(String to);

    Mono<MailResult> sendForgotPasswordEmail(MailInformation mailInformation);

    Mono<MailResult> sendEmailVerification(MailInformation mailInformation, String link);

    Mono<MailResult> sendServiceProviderConfirmation(String to, Status status);
}
