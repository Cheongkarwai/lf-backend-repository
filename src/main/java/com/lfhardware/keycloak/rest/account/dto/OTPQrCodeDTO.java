package com.lfhardware.keycloak.rest.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTPQrCodeDTO {

    private String secret;

    @JsonProperty("qr_code_base64")
    private String qrCodeBase64;
}
