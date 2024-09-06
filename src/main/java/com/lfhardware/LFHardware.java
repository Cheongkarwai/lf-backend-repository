package com.lfhardware;

import com.lfhardware.configuration.EasyParcelConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class LFHardware{

    public static void main(String[] args) {
        SpringApplication.run(LFHardware.class, args);
    }

}
