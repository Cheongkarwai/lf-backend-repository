package com.lfhardware.configuration;

import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Bucket4jConfiguration {

    @Bean
    public Bucket bucket() {
        return Bucket.builder()
                .addLimit(limit-> limit.capacity(50).refillGreedy(10, Duration.ofMinutes(5))).build();
    }
}
