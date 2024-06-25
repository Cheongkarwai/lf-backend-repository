package com.lfhardware.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("description")
    private String description;

    private double rating;
}
