package com.lfhardware.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.provider.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderDTO {

    private String id;

    private String name;

    @JsonProperty("contact_info")
    private ContactInfoDTO contactInfo;

    @JsonProperty("is_verified")
    private boolean isVerified;

    private double rating;

    @JsonProperty("social_media")
    private SocialMediaLinkDTO socialMedia;

    @JsonProperty("stripe_account_id")
    private String stripeAccountId;

    @JsonProperty("front_identity_card")
    private String frontIdentityCard;

    @JsonProperty("back_identity_card")
    private String backIdentityCard;

    @JsonProperty("ssm")
    private String ssm;

    private Status status;

    @JsonProperty("business_profile_image")
    private String businessProfileImage;
}
