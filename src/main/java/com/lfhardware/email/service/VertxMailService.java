package com.lfhardware.email.service;

import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class VertxMailService implements  IEmailService{

    private MailClient mailClient;

    public VertxMailService(MailClient mailClient){
        this.mailClient = mailClient;
    }


    @Override
    public Mono<MailResult> sendForgotPasswordEmail(String to) {

        MailMessage message = new MailMessage();
        message.setFrom("cheongkarwai10@gmail.com");
        message.setTo("cheongkarwai5@gmail.com");
        message.setText("this is the plain message text");
        message.setHtml("this is html text <a href=\"http://vertx.io\">vertx.io</a>");

        return Mono.fromCompletionStage(mailClient.sendMail(message).toCompletionStage());
    }
}
