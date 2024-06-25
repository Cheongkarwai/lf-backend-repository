package com.lfhardware.email.service;

import com.lfhardware.auth.repository.IUserRepository;
import io.vertx.ext.mail.MailResult;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Persistence;
import org.hibernate.reactive.stage.Stage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

//@Service
//public class JavaMailService implements IEmailService{
//
//    private final JavaMailSender javaMailSender;
//
//    private final IUserRepository userRepository;
//
//    private Stage.SessionFactory sessionFactory;
//
//
//    @PostConstruct
//    public void init(){
//        sessionFactory = Persistence.createEntityManagerFactory("postgres").unwrap(Stage.SessionFactory.class);
//    }
//    public JavaMailService(JavaMailSender javaMailSender,IUserRepository userRepository){
//        this.javaMailSender = javaMailSender;
//        this.userRepository = userRepository;
//    }
//
//
//    @Override
//    public Mono<MailResult> sendForgotPasswordEmail(String to) {
//
////        return Mono.fromFuture(sessionFactory.withTransaction((session, transaction) -> userRepository.findById(session,to).thenApply(user->{
////
////            MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
////
////                String changePasswordPage = "http://localhost:4200/change-password";
////                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false);
////                mimeMessageHelper.setSubject("Forgot Password");
////                mimeMessageHelper.setTo(to);
////                mimeMessageHelper.setText(
////                        "<h5>Click <a href='"+changePasswordPage+"?username="+"'>here</a> to change password</h5>", true);
////
////            };
////
////            try {
////                this.javaMailSender.send(mimeMessagePreparator);
////            }
////            catch (MailException ex) {
////                // simply log it and go on...
////                System.err.println(ex.getMessage());
////            }
////            return "Success";
////        })).toCompletableFuture());
//
//        return null;
////                .switchIfEmpty(Mono.error(new NoSuchElementException("Hi")));
//    }
//}
