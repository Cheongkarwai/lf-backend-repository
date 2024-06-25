package com.lfhardware.configuration;

import io.vertx.core.Vertx;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.StartTLSOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfiguration {

    @Bean
    public MailClient mailClient(){
        MailConfig mailConfig = new MailConfig();
        mailConfig.setUsername("cheongkarwai5@gmail.com");
        mailConfig.setPassword("rmxbxpfraiqummqn");
        mailConfig.setStarttls(StartTLSOptions.REQUIRED);
        mailConfig.setPort(587);
        mailConfig.setHostname("smtp.gmail.com");

        return MailClient.create(Vertx.vertx(),mailConfig);
    }
}
