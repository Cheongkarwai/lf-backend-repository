package com.lfhardware.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    public static final String citiesCache = "cities";

    public static final String appointmentCache = "appointmentCache";

    public static final String statesCache = "states";

    public static final String countriesCache = "countries";

    public static final String serviceCache = "serviceCache";

    public static final String productCache = "productCache";

    public static final String categoryCache = "categoryCache";

    public static final String brandCache = "brandCache";

    public static final String cartCache = "cart";

    public static final String faqCache = "faqCache";

    public static final String customerCache = "customerCache";

    public static final String customerAppointmentCache = "customerAppointmentCache";

    public static final String serviceProviderCache = "serviceProviderCache";

    public static final String providerServiceCache = "providerServiceCache";

    public static final String serviceProviderDetailsCache = "serviceProviderDetailsCache";

    public static final String reviewCache = "serviceProviderReviewCache";

    public static final String serviceProviderReviewCache = "serviceProviderReviewCache";

    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(List.of(citiesCache,statesCache,countriesCache,
                serviceCache,productCache,categoryCache,brandCache,cartCache, serviceProviderReviewCache, faqCache, customerCache,
                customerAppointmentCache, serviceProviderCache, serviceProviderDetailsCache, reviewCache, appointmentCache, providerServiceCache));
        cacheManager.setCaffeine(caffeineCache());
        cacheManager.setAsyncCacheMode(true);
        return cacheManager;
    }

    @Bean
    Caffeine<Object,Object> caffeineCache(){
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterAccess(365, TimeUnit.DAYS)
                .expireAfterWrite(1,TimeUnit.MINUTES);
    }
}
