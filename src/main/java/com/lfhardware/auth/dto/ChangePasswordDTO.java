package com.lfhardware.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDTO {

    @NotBlank(message = "Email address is required")
    @Email(message = "Email is invalid")
    @JsonProperty("email_address")
    private String emailAddress;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirmation password is required")
    @JsonProperty("confirmation_password")
    private String confirmationPassword;
}
