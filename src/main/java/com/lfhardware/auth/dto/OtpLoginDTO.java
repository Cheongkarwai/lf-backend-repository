package com.lfhardware.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtpLoginDTO {
    @NotBlank(message = "Email address must not be blank")
    @Email(message = "Invalid email address")
    @JsonProperty("email_address")
    private String receiver;
}
