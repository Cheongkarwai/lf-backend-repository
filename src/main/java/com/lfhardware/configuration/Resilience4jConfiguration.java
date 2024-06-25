package com.lfhardware.configuration;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jConfiguration {

    @Bean
    public RateLimiterRegistry rateLimiterRegistry(){
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(60))
                .limitForPeriod(5)
                .timeoutDuration(Duration.ofSeconds(25))
                .build();
        return RateLimiterRegistry.of(config);
    }
}
