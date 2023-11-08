package com.lfhardware.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordRecoveryDTO {

    @NotBlank(message = "Email address must not be blank")
    @Email(message = "Invalid email address")
    @JsonProperty("email_address")
    private String emailAddress;

    private String url;
}
