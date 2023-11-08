package com.lfhardware.email.service;

import io.vertx.ext.mail.MailResult;
import reactor.core.publisher.Mono;

public interface IEmailService {

    Mono<MailResult> sendForgotPasswordEmail(String to);
}
