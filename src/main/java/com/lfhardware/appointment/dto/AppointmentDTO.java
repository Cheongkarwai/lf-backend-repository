package com.lfhardware.appointment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.customer.dto.CustomerDTO;
import com.lfhardware.provider.dto.ServiceProviderDTO;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private UUID id;

    private ServiceDTO service;

    private CustomerDTO customer;

    @JsonProperty("service_provider")
    private ServiceProviderDTO serviceProvider;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("booking_datetime")
    private LocalDateTime bookingDatetime;

    @JsonProperty("estimated_price")
    private BigDecimal estimatedPrice;

    @JsonProperty("is_paid")
    private boolean isPaid;

    private AppointmentStatus status;

    @JsonProperty("completion_datetime")
    private LocalDateTime completionDateTime;

    @JsonProperty("status_last_update")
    private LocalDateTime statusLastUpdate;

    @JsonProperty("confirmation_datetime")
    private LocalDateTime confirmationDatetime;

    @JsonProperty("job_started_datetime")
    private LocalDateTime jobStartedDatetime;

    @JsonProperty("job_completion_datetime")
    private LocalDateTime jobCompletionDatetime;

    @JsonProperty("review_datetime")
    private LocalDateTime reviewDatetime;

    @JsonProperty("appointment_completion_images")
    private List<AppointmentImageDTO> appointmentImages;


}
