package com.lfhardware.customer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.auth.dto.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private String id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private AddressDTO address;

    private List<AppointmentDTO> appointments;

    @JsonProperty("email_verified")
    private boolean isEmailVerified;

    @JsonProperty("enabled")
    private boolean isEnabled;
}
