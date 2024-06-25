package com.lfhardware.appointment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentIdInput {

    @JsonProperty("service_provider_id")
    private String serviceProviderId;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("service_id")
    private Long serviceId;

    @JsonProperty("customer_id")
    private String customerId;
}
