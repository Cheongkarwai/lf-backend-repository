package com.lfhardware.review.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewInput {

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("user_id")
    private String userId;

    private String description;

    private double rating;
}
