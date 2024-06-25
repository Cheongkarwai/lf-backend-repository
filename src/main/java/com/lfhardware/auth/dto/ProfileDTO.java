package com.lfhardware.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {

//    @NotBlank(message = "Email address must not be blank")
//    @Email(message = "Invalid email address")
    @JsonProperty("email_address")
    private String emailAddress;

//    @NotBlank(message = "Phone number must not be blank")
    @JsonProperty("phone_number")
    private String phoneNumber;

    protected AddressDTO address;
}
