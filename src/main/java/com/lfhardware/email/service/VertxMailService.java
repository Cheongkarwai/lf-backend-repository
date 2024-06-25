package com.lfhardware.email.service;

import com.lfhardware.email.dto.MailInformation;
import com.lfhardware.provider.domain.Status;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailResult;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringWebFluxTemplateEngine;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Random;

@Service
public class VertxMailService implements IEmailService {

    private final MailClient mailClient;

    private final SpringWebFluxTemplateEngine templateEngine;

    public VertxMailService(MailClient mailClient, SpringWebFluxTemplateEngine templateEngine) {
        this.mailClient = mailClient;
        this.templateEngine = templateEngine;
    }

    @Override
    public Mono<MailResult> sendOneTimeLoginPasscode(String to) {
        MailMessage message = new MailMessage();
        message.setFrom("cheongkarwai5@gmail.com");
        message.setTo(to);
        message.setText("One time login code");
        Random random = new Random();
        int randomNumber = random.nextInt(999999 - 100000 + 1) + 100000;
        message.setHtml("Your code is <a>" + randomNumber + "</a>");

        return Mono.fromCompletionStage(mailClient.sendMail(message).toCompletionStage());
    }

    @Override
    public Mono<MailResult> sendForgotPasswordEmail(MailInformation mailInformation) {
        MailMessage message = new MailMessage();
        message.setFrom("cheongkarwai5@gmail.com");
        message.setTo(mailInformation.getRecipient().getEmailAddress());
        message.setSubject(mailInformation.getSubject());
        String html = templateEngine.process("forgot-password", mailInformation.getContext());
        message.setHtml(html);

        System.out.println(message);
//        message.setText("One time login code");
//        Random random = new Random();
//        int randomNumber = random.nextInt(999999 - 100000 + 1) + 100000;
//        message.setHtml("Your code is <a>" + randomNumber + "</a>");
//
        return Mono.fromCompletionStage(mailClient.sendMail(message).toCompletionStage());
    }

    @Override
    public Mono<MailResult> sendEmailVerification(MailInformation mailInformation, String link) {

//        ActionCodeSettings actionCodeSettings = ActionCodeSettings.builder()
//                .setUrl("http://localhost:4200")
//                .setHandleCodeInApp(true)
//                .setIosBundleId("com.example.ios")
//                .setAndroidPackageName("com.example.android")
//                .setAndroidInstallApp(true)
//                .setAndroidMinimumVersion("12")
//                .setDynamicLinkDomain("coolapp.page.link")
//                .build();

//        String link = FirebaseAuth.getInstance().generateEmailVerificationLink(
//                to, actionCodeSettings);
        MailMessage message = new MailMessage();
        message.setFrom("cheongkarwai5@gmail.com");
        message.setTo(mailInformation.getRecipient().getEmailAddress());
        message.setSubject(mailInformation.getSubject());
        Context context = new Context();
        String html = templateEngine.process("verify-email", mailInformation.getContext());
        message.setHtml(html);

        return Mono.fromCompletionStage(mailClient.sendMail(message).toCompletionStage());
    }

    @Override
    public Mono<MailResult> sendServiceProviderConfirmation(String to, Status status) {
        MailMessage mailMessage = new MailMessage();
        Context context = new Context();
        context.setVariable("status", status.toString().toLowerCase());
        String htmlContent = this.templateEngine.process("email", context);
        mailMessage.setHtml(htmlContent);
        mailMessage.setFrom("cheongkarwai5@gmail.com");
        mailMessage.setTo(to);
        return Mono.fromCompletionStage(mailClient.sendMail(mailMessage).toCompletionStage());
    }
}
