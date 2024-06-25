package com.lfhardware.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.provider.dto.ServiceProviderInput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderAccountDTO {

    @JsonProperty("account")
    private UserAccountDTO account;

    @JsonProperty("service_provider")
    private ServiceProviderInput serviceProvider;
}
