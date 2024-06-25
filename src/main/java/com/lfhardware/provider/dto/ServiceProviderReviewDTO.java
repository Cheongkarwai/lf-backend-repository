package com.lfhardware.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.customer.dto.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderReviewDTO {

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private String description;

    private double rating;

    @JsonProperty("service_provider")
    private ServiceProviderDTO serviceProvider;

    private CustomerDTO customer;

}
