package com.lfhardware.config;

import io.vertx.core.Vertx;
import io.vertx.core.spi.VerticleFactory;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.StartTLSOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

    @Bean
    public MailClient mailClient(){
        io.vertx.ext.mail.MailConfig mailConfig = new io.vertx.ext.mail.MailConfig();
        mailConfig.setUsername("cheongkarwai10@gmail.com");
        mailConfig.setPassword("iqjsbvtkuoncabqo");
        mailConfig.setStarttls(StartTLSOptions.REQUIRED);
        mailConfig.setPort(587);
        mailConfig.setHostname("smtp.gmail.com");

        return MailClient.create(Vertx.vertx(),mailConfig);
    }
}
