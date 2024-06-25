package com.lfhardware.configuration;

import com.lfhardware.shipment.client.EasyParcelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EasyParcelConfiguration {


    @Bean
    public EasyParcelClient easyParcelClient(@Value("${easy-parcel.url}") String url, @Value("${easy-parcel.api-key}") String apiKey) {
        return new EasyParcelClient(url, apiKey);
    }
}
