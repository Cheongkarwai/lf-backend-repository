package com.lfhardware.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.auth.dto.AddressDTO;
import com.lfhardware.provider.domain.Status;
import com.lfhardware.provider_business.dto.ServiceDTO;
import com.lfhardware.shared.BaseAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderDetailsDTO {

    private String id;

    private String name;

    @JsonProperty("contact_info")
    private ContactInfoDTO contactInfo;

    @JsonProperty("is_verified")
    private boolean isVerified;

    private double rating;

    private List<ServiceDTO> services;

    private String overview;

    @JsonProperty("state_coverages")
    private Set<String> stateCoverages;

    @JsonProperty("country_coverages")
    private Set<String> countryCoverages;

    @JsonProperty("city_coverages")
    private Set<String> cityCoverages;

    private Status status;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("business_profile_image")
    private String businessProfileImage;

    private Set<ServiceProviderReviewDTO> reviews;

    @JsonProperty("social_media")
    private SocialMediaLinkDTO socialMedia;

    @JsonProperty("stripe_account_id")
    private String stripeAccountId;

    @JsonProperty("front_identity_card")
    private String frontIdentityCard;

    @JsonProperty("back_identity_card")
    private String backIdentityCard;

    private AddressDTO address;
}
