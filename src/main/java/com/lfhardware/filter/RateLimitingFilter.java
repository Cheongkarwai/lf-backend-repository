package com.lfhardware.filter;

import com.lfhardware.exception.RateLimitException;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class RateLimitingFilter implements WebFilter {

    private final Bucket bucket;

    private final CacheManager cacheManager;

    public RateLimitingFilter(Bucket bucket, CacheManager cacheManager){
        this.bucket = bucket;
        this.cacheManager = cacheManager;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

//        ServerHttpRequest serverRequest = exchange.getRequest();
//        Optional<String> ipAddressOptional = Optional.ofNullable(serverRequest.getHeaders().getFirst("X-Forwarded-From"));
//
//        String ipAddress = ipAddressOptional.orElse(Objects.requireNonNull(serverRequest.getRemoteAddress()).getAddress().getAddress().toString());
//
//        log.info("Remaining tokens in bucket {} ", bucket.getAvailableTokens());
//        Optional<Bucket> bucketOptional = Optional.ofNullable(cacheManager.getCache("rateLimitCache").get(ipAddress, Bucket.class));
//
//        Bucket bucket = bucketOptional.orElseGet(()->{
//            Bucket newBucket = Bucket.builder()
//                    .addLimit(limit-> limit.capacity(50).refillGreedy(10, Duration.ofMinutes(5))).build();
//            cacheManager.getCache("rateLimitCache").put(ipAddress, newBucket);
//            return newBucket;
//        });
//
//        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
//
//        //Set rate limit headers
//        exchange.getResponse().getHeaders().set("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()));
//        exchange.getResponse().getHeaders().set("X-Rate-Limit-Retry-After-Seconds", String.valueOf(probe.getNanosToWaitForRefill() / 1_000_000_000));
//        exchange.getResponse().getHeaders().set("X-Rate-Limit-Reset", String.valueOf(LocalDateTime.now().plusSeconds((int) (probe.getNanosToWaitForReset() / 1_000_000_000))));
//
//        if(!probe.isConsumed()){
//            return Mono.error(new RateLimitException("Exceed limit"));
//        }
        return chain.filter(exchange);
    }
}
